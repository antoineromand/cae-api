package com.pickandeat.authentication.infrastructure.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
}
