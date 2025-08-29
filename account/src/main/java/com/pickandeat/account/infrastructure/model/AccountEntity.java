package com.pickandeat.account.infrastructure.model;

import com.pickandeat.account.domain.account.Account;
import com.pickandeat.shared.enums.RoleName;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Table(
    name = "account",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"phoneNumber"})},
    indexes = {@Index(name = "idx_account_phoneNumber", columnList = "phoneNumber")})
@Entity()
public class AccountEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "account_id", updatable = false, nullable = false)
  private Long id;

  @Column(name = "firstName", updatable = true, nullable = false)
  private String firstName;

  @Column(name = "lastName", updatable = true, nullable = false)
  private String lastName;

  @Column(name = "birth_date", updatable = true, nullable = false)
  private String birthDate;

  @Column(name = "phone_number", updatable = true, nullable = false)
  private String phoneNumber;

  @Column(name = "created_at", updatable = false, nullable = false)
  @CreatedDate
  private Instant createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", updatable = true, nullable = true)
  private Instant updatedAt;

  @Column(name = "role", updatable = false, nullable = false)
  private String role;

  @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, optional = true)
  private AccountProInformationsEntity accountProInformations;

  public AccountEntity(
      Long id,
      String firstName,
      String lastName,
      String birthDate,
      String phoneNumber,
      Instant createdAt,
      Instant updatedAt,
      String role) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDate = birthDate;
    this.phoneNumber = phoneNumber;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.role = role;
  }

  public AccountEntity() {}

  public Account toDomain(String email) {
    return new Account(
        this.id,
        this.lastName,
        this.firstName,
        RoleName.valueOf(this.role),
        this.phoneNumber,
        email,
        this.birthDate,
        Date.from(this.createdAt),
        Objects.isNull(this.updatedAt) ? null : Date.from(this.updatedAt),
        null);
  }

  public static AccountEntity fromDomain(Account account) {
    return new AccountEntity(
        account.getId(),
        account.getLastName(),
        account.getFirstName(),
        account.getAccountBirthDate().date(),
        account.getAccountPhoneNumber().phoneNumber(),
        account.getAccountCreatedDate().toInstant(),
        Objects.isNull(account.getAccountUpdatedDate())
            ? null
            : account.getAccountUpdatedDate().toInstant(),
        account.getRole().toString());
  }

  public Long getId() {
    return id;
  }
}
