package com.pickandeat.authentication.domain.valueobject;

public record Scope(String action, String target) {
  public String getScope() {
    return action.toLowerCase() + ":" + target.toLowerCase();
  }
}
