package com.pickandeat.authentication.domain;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.domain.valueobject.Scope;

public class Credentials {
    private final UUID id;
    private final String email;
    private String password;
    private final Role role;
    private final Set<Scope> scopes;
    private final Date createdAt;
    private Date updatedAt;
    
    public Credentials(UUID id, String email, String password, Role role, Set<Scope> scopes, Date createdAt,
            Date updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.scopes = scopes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Set<Scope> getScopes() {
        return scopes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean hasAdminRole() {
        return role == Role.ADMIN;
    }

    public boolean hasConsummerRole() {
        return role == Role.CONSUMMER;
    }

    public boolean hasProRole() {
        return role == Role.PRO;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void changePassword(String password) {
        this.password = password;
        this.updatedAt = new Date();
    }

    private boolean hasScope(String action, String target) {
        return scopes.contains(new Scope(action, target));
    }

    public boolean hasWildcardScope() {
        return hasScope("*", "*");
    }

    public boolean canAccess(String action, String target) {
        return hasWildcardScope() || hasScope(action, target);
    }
}
