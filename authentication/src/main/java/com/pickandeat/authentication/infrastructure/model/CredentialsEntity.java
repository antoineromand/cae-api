package com.pickandeat.authentication.infrastructure.model;

import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.domain.valueobject.Scope;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(
        name = "credentials",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})},
        indexes = {@Index(name = "idx_credentials_email", columnList = "email")})
@Entity()
@EntityListeners(AuditingEntityListener.class)
public class CredentialsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "credentials_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "email", updatable = true, nullable = false, length = 200)
    private String email;

    @Column(name = "password", updatable = true, nullable = false, length = 200)
    private String password;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", updatable = true, nullable = true)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    public CredentialsEntity() {
    }

    public CredentialsEntity(
            UUID id,
            String email,
            String password,
            Instant createdAt,
            Instant updatedAt,
            RoleEntity roleEntity) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roleEntity = roleEntity;
    }

    public static CredentialsEntity fromDomain(Credentials credentials, RoleEntity roleEntity) {
        Instant updatedAt =
                credentials.getUpdatedAt() != null ? credentials.getUpdatedAt().toInstant() : null;
        return new CredentialsEntity(
                credentials.getId(),
                credentials.getEmail(),
                credentials.getPassword(),
                credentials.getCreatedAt().toInstant(),
                updatedAt,
                roleEntity);
    }

    public Credentials toDomain() {
        Set<Scope> scopes =
                roleEntity.getScopes().stream()
            .map(scopeEntity -> new Scope(scopeEntity.getAction(), scopeEntity.getTarget()))
            .collect(Collectors.toSet());

        RoleName roleName = RoleName.valueOf(roleEntity.getName());
        Role domainRole = new Role(roleName, scopes);

        return new Credentials(
                id,
                email,
                password,
                domainRole,
                Date.from(createdAt),
                updatedAt != null ? Date.from(updatedAt) : null);
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public RoleEntity getRoleEntity() {
        return roleEntity;
    }
}
