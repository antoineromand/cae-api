package com.pickandeat.authentication.infrastructure.model;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

@Table(name = "credentials", uniqueConstraints = {
      @UniqueConstraint(columnNames = { "email" })
}, indexes = {
      @Index(name = "idx_credentials_email", columnList = "email")
})
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
}
