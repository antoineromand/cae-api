package com.clickandeat.account.infrastructure.model;

import jakarta.persistence.*;

@Entity()
@Table(
    name = "pro_informations",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"siret"})},
    indexes = {@Index(name = "idx_credentials_phoneNumber", columnList = "phoneNumber")})
public class AccountProInformationsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
