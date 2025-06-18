package com.pickandeat.api.config;

import com.pickandeat.authentication.domain.valueobject.Scope;
import com.pickandeat.authentication.infrastructure.model.RoleEntity;
import com.pickandeat.authentication.infrastructure.repository.RoleEntityJPARepository;
import com.pickandeat.shared.token.application.TokenService;
import com.pickandeat.shared.token.domain.TokenPayload;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final RoleEntityJPARepository roleEntityJPARepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractBearerToken(request);
        if (token == null || token.isBlank()) {
            reject(response, "Missing Bearer token");
            return;
        }
        if (!this.tokenService.isAccessTokenValid(token)) {
            reject(response, "Token not valid");
            return;
        }

        TokenPayload tokenPayload = this.tokenService.extractPayload(token);

        Set<Scope> scopes = this.resolveScopesFromRole(tokenPayload.getRole());

        CustomUserDetails customUserDetails = new CustomUserDetails(tokenPayload.getUserId(), tokenPayload.getRole(), scopes);
        authenticateUser(request, customUserDetails);
        filterChain.doFilter(request, response);

    }

    private void reject(HttpServletResponse response, String message) throws IOException {
        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }

    private String extractBearerToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring("Bearer ".length()).trim();
        }
        return null;
    }

    private Set<Scope> resolveScopesFromRole(String roleName) {
        return this.roleEntityJPARepository.findByName(roleName)
                .map(RoleEntity::getScopes)
                .map(set -> set.stream()
                        .map(se -> new Scope(se.getAction(), se.getTarget()))
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    private void authenticateUser(HttpServletRequest request, CustomUserDetails userDetails) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
