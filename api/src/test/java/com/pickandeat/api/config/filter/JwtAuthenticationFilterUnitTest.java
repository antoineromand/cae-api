package com.pickandeat.api.config.filter;

import com.pickandeat.authentication.infrastructure.model.RoleEntity;
import com.pickandeat.authentication.infrastructure.model.ScopeEntity;
import com.pickandeat.authentication.infrastructure.repository.RoleEntityJPARepository;
import com.pickandeat.shared.token.TokenPayload;
import com.pickandeat.shared.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
public class JwtAuthenticationFilterUnitTest {

    private TokenService tokenService;
    private RoleEntityJPARepository roleRepo;
    private JwtAuthenticationFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        tokenService = mock(TokenService.class);
        roleRepo = mock(RoleEntityJPARepository.class);
        filter = new JwtAuthenticationFilter(tokenService, roleRepo);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldNotFilterForPublicUrl() throws Exception {
        when(request.getRequestURI()).thenReturn("/public/resource");

        boolean result = filter.shouldNotFilter(request);

        assertTrue(result);
    }

    @Test
    void shouldRejectWhenTokenMissing() throws Exception {
        when(request.getRequestURI()).thenReturn("/private/endpoint");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Bearer token");
        verifyNoMoreInteractions(tokenService, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldRejectWhenTokenInvalid() throws Exception {
        when(request.getRequestURI()).thenReturn("/private/endpoint");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer invalidToken");
        when(tokenService.isAccessTokenValid("invalidToken")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token not valid");
        verify(tokenService).isAccessTokenValid("invalidToken");
        verifyNoMoreInteractions(tokenService, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldAuthenticateWhenTokenValidAndRoleFound() throws Exception {
        String token = "validToken";
        UUID userId = UUID.randomUUID();
        String role = "ADMIN";

        when(request.getRequestURI()).thenReturn("/private/data");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(tokenService.isAccessTokenValid(token)).thenReturn(true);

        TokenPayload payload = new TokenPayload(userId, role);
        when(tokenService.extractPayload(token)).thenReturn(payload);

        RoleEntity roleEntity = mock(RoleEntity.class);
        ScopeEntity scopeEntity = mock(ScopeEntity.class);
        when(scopeEntity.getAction()).thenReturn("READ");
        when(scopeEntity.getTarget()).thenReturn("RESOURCE");

        when(roleRepo.findByNameWithScopes(role)).thenReturn(Optional.of(roleEntity));
        when(roleEntity.getScopes()).thenReturn(Set.of(scopeEntity));
        filter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(
                userId,
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUserId());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateWithEmptyScopesWhenRoleNotFound() throws Exception {
        String token = "validToken";
        UUID userId = UUID.randomUUID();
        String role = "UNKNOWN";

        when(request.getRequestURI()).thenReturn("/private/data");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(tokenService.isAccessTokenValid(token)).thenReturn(true);

        TokenPayload payload = new TokenPayload(userId, role);
        when(tokenService.extractPayload(token)).thenReturn(payload);

        when(roleRepo.findByNameWithScopes(role)).thenReturn(Optional.empty());

        filter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(
                userId,
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUserId());

        verify(filterChain).doFilter(request, response);
    }
}
