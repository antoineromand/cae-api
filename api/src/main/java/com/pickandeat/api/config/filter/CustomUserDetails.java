package com.pickandeat.api.config.filter;

import com.pickandeat.authentication.domain.valueobject.Scope;
import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private final UUID userId;
    private final String role;
    private final Set<Scope> scopes;

    public CustomUserDetails(UUID userId, String role, Set<Scope> scopes) {
        this.userId = userId;
        this.role = role;
        this.scopes = scopes;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public Set<Scope> getScopes() {
        return scopes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_" + role);
        scopes.forEach(
                scope ->
                        authorities.add(
                                () ->
                                        "SCOPE_" + scope.action().toUpperCase() + ":" + scope.target().toUpperCase()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
