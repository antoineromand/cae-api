package com.clickandeat.authentication.infrastructure.model;

import jakarta.persistence.*;

@Table(name = "scope")
@Entity()
public class ScopeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "scope_id")
  private int id;

  @Column(name = "action", length = 50, nullable = false)
  private String action;

  @Column(name = "target", length = 50, nullable = false)
  private String target;

  public ScopeEntity() {}

  public int getId() {
    return id;
  }

  public String getAction() {
    return action;
  }

  public String getTarget() {
    return target;
  }
}
