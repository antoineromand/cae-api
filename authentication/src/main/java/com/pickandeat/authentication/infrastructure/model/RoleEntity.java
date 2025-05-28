package com.pickandeat.authentication.infrastructure.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Table(name = "role")
@Entity()
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name = "CONSUMMER";

    // Set<Scope> many to many
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_scope", 
        joinColumns = { @JoinColumn(name = "role_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "scope_id") }
    )
    private Set<ScopeEntity> scopes = new HashSet<>();
}
