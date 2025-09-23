package com.clickandeat.authentication.domain;

import com.clickandeat.authentication.domain.valueobject.Role;
import java.util.Date;
import java.util.UUID;

public class Credentials {
  private final UUID id;
  private final String email;
  private String password;
  private final Role role;
  private final Date createdAt;
  private Date updatedAt;

  public Credentials(
      UUID id, String email, String password, Role role, Date createdAt, Date updatedAt) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.role = role;
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

  public Date getCreatedAt() {
    return createdAt;
  }

  public boolean hasAdminRole() {
    return role.isAdmin();
  }

  public boolean hasConsummerRole() {
    return role.isConsumer();
  }

  public boolean hasProRole() {
    return role.isPro();
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void changePassword(String password) {
    this.password = password;
    this.updatedAt = new Date();
  }

  public boolean canAccess(String action, String target) {
    return role.hasWildcardScope() || role.hasScope(action, target);
  }
}
