package com.pickandeat.account.domain.account.pro;

public class AccountProInformations {
  private final Long id;
  private final String kbis_ref;
  private final String siret;
  private final String legal_name;
  private final String legal_form;
  private final Localisation localisation;

  public AccountProInformations(
          Long id,
      String kbis_ref,
      String siret,
      String legal_name,
      String legal_form,
      String address1,
      String address2,
      String address3,
      String city,
      String postalCode,
      String country) {
    this.id = id;
    this.kbis_ref = kbis_ref;
    this.siret = siret;
    this.legal_name = legal_name;
    this.legal_form = legal_form;
    this.localisation = new Localisation(address1, address2, address3, city, postalCode, country);
  }

  public Long getId() {
    return id;
  }

  public String getKbis_ref() {
    return kbis_ref;
  }

  public String getSiret() {
    return siret;
  }

  public String getLegal_name() {
    return legal_name;
  }

  public String getLegal_form() {
    return legal_form;
  }

  public Localisation getLocalisation() {
    return localisation;
  }
}
