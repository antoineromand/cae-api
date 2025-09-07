package com.clickandeat.account.infrastructure.model;

import com.clickandeat.account.domain.account.Account;
import com.clickandeat.shared.enums.RoleName;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Table(
    name = "account",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"phoneNumber"})},
    indexes = {@Index(name = "idx_account_phoneNumber", columnList = "phoneNumber")})
@Entity()
public class AccountEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_id", updatable = false, nullable = false, columnDefinition = "serial")
  private Long id;

  @Column(name="credentials_id", nullable = false, updatable = false)
  private UUID credentialsId;

  @Column(name = "firstName", updatable = true, nullable = false)
  private String firstName;

  @Column(name = "lastName", updatable = true, nullable = false)
  private String lastName;

  @Column(name = "birth_date", updatable = true, nullable = false)
  private LocalDate birthDate;

  @Column(name = "phone_number", updatable = true, nullable = false)
  private String phoneNumber;

  @Column(name = "created_at", updatable = false, nullable = false)
  @CreatedDate
  private Instant createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", updatable = true, nullable = true)
  private Instant updatedAt;

  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  @Column(name = "role_type", updatable = false, nullable = false)
  private RoleName roleType;

  @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, optional = true)
  private AccountProInformationsEntity accountProInformations;

  public AccountEntity(
      Long id,
      String firstName,
      String lastName,
      UUID credentialsId,
      LocalDate birthDate,
      String phoneNumber,
      Instant createdAt,
      Instant updatedAt,
      RoleName role) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDate = birthDate;
    this.phoneNumber = phoneNumber;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.roleType = role;
    this.credentialsId = credentialsId;
  }

  public AccountEntity() {}

  public Account toDomain(String email) {
    return new Account(
        this.id,
        this.lastName,
        this.firstName,
        this.roleType,
        this.phoneNumber,
        email,
        this.birthDate.toString(),
        Date.from(this.createdAt),
        Objects.isNull(this.updatedAt) ? null : Date.from(this.updatedAt),
        null);
  }

  public static AccountEntity fromDomain(Account account, UUID credentialsId) {
    return new AccountEntity(
        account.getId(),
        account.getLastName(),
        account.getFirstName(),
        credentialsId,
        LocalDate.parse(account.getAccountBirthDate().date()),
        account.getAccountPhoneNumber().phoneNumber(),
        account.getAccountCreatedDate().toInstant(),
        Objects.isNull(account.getAccountUpdatedDate())
            ? null
            : account.getAccountUpdatedDate().toInstant(),
        account.getRole());
  }

  public Long getId() {
    return id;
  }
}
