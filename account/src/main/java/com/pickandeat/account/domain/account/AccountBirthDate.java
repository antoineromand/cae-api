package com.pickandeat.account.domain.account;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;

public record AccountBirthDate(String date) {
  public int getAge() throws ParseException {
    return Period.between(LocalDate.parse(date), LocalDate.now()).getYears();
  }
}
