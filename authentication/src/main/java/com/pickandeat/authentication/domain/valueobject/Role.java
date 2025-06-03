package com.pickandeat.authentication.domain.valueobject;

import java.util.Set;

import com.pickandeat.authentication.domain.enums.RoleName;

public record Role(RoleName name, Set<Scope> scopes) {

    public boolean hasScope(String action, String target) {
        return scopes.contains(new Scope(action, target));
    }

    public boolean hasWildcardScope() {
        return hasScope("*", "*");
    }

    public boolean is(RoleName roleName) {
        return this.name == roleName;
    }

    public boolean isAdmin() {
        return is(RoleName.ADMIN);
    }

    public boolean isConsumer() {
        return is(RoleName.CONSUMER);
    }

    public boolean isPro() {
        return is(RoleName.PRO);
    }
}
