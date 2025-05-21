package com.pickandeat.authentication.domain;

public class Scope {
    private int id;
    private String action;
    private String target;

    public Scope(int id, String action, String target) {
        this.id = id;
        this.action = action;
        this.target = target;
    }

    public String getScope() {
        return this.action.toLowerCase() + ":" + this.target.toLowerCase();
    }

    public boolean isAdmin() {
        return this.action.equals("*") && this.target.equals("*");
    }
}
