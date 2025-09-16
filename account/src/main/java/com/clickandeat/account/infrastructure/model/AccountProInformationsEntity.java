package com.clickandeat.account.infrastructure.model;

import com.clickandeat.account.domain.account.pro.AccountProInformations;
import jakarta.persistence.*;
import java.util.UUID;

@Entity()
@Table(
    name = "pro_informations",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"siret"})})
public class AccountProInformationsEntity {
  @Id
  @Column(name = "account_id", updatable = false, nullable = false)
  private Long id;

  @OneToOne
  @MapsId
  @JoinColumn(name = "account_id")
  private AccountEntity account;

  @Column(name = "kbis_ref", updatable = true, nullable = true)
  private String kbis_ref;

  @Column(name = "siret", updatable = true, nullable = false)
  private String siret;

  @Column(name = "address1", updatable = true, nullable = false)
  private String address1;

  @Column(name = "address2", updatable = true, nullable = true)
  private String address2;

  @Column(name = "address3", updatable = true, nullable = true)
  private String address3;

  @Column(name = "city", updatable = true, nullable = true)
  private String city;

  @Column(name = "cp", updatable = true, nullable = true)
  private String cp;

  @Column(name = "country", updatable = true, nullable = true)
  private String country;

  @Column(name = "legal_form", updatable = true, nullable = true)
  private String legalForm;

  @Column(name = "legal_name", updatable = true, nullable = true)
  private String legalName;

  public AccountProInformationsEntity(
      Long id,
      AccountEntity account,
      String kbis_ref,
      String siret,
      String address1,
      String address2,
      String address3,
      String city,
      String cp,
      String country,
      String legalForm,
      String legalName) {
    this.id = id;
    this.account = account;
    this.kbis_ref = kbis_ref;
    this.siret = siret;
    this.address1 = address1;
    this.address2 = address2;
    this.address3 = address3;
    this.city = city;
    this.cp = cp;
    this.country = country;
    this.legalForm = legalForm;
    this.legalName = legalName;
  }

  public AccountProInformationsEntity() {}

  public Long getId() {
    return id;
  }

  public AccountProInformations toDomain() {
    return new AccountProInformations(
        this.account.getId(),
        this.kbis_ref,
        this.siret,
        this.legalName,
        this.legalForm,
        this.address1,
        this.address2,
        this.address3,
        this.city,
        this.cp,
        this.country);
  }

  public static AccountProInformationsEntity fromDomain(
      AccountProInformations accountProInformations,
      AccountEntity accountEntity,
      UUID credentialsId) {
    return new AccountProInformationsEntity(
        null,
        accountEntity,
        accountProInformations.getKbis_ref(),
        accountProInformations.getSiret(),
        accountProInformations.getLocalisation().address1(),
        accountProInformations.getLocalisation().address2(),
        accountProInformations.getLocalisation().address3(),
        accountProInformations.getLocalisation().city(),
        accountProInformations.getLocalisation().postalCode(),
        accountProInformations.getLocalisation().country(),
        accountProInformations.getLegal_form(),
        accountProInformations.getLegal_name());
  }
}
