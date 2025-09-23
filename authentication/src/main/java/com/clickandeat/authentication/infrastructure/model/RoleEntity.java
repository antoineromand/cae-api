package com.clickandeat.authentication.infrastructure.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "role")
@Entity()
public class RoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private int id;

  @Column(name = "name", length = 50, nullable = false, unique = true)
  private String name = "CONSUMER";

  // Set<Scope> many to many
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "role_scope",
      joinColumns = {@JoinColumn(name = "role_id")},
      inverseJoinColumns = {@JoinColumn(name = "scope_id")})
  private Set<ScopeEntity> scopes = new HashSet<>();

  public RoleEntity() {}

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<ScopeEntity> getScopes() {
    return scopes;
  }
}
