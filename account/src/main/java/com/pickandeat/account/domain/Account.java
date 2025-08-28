package com.pickandeat.account.domain;

import com.pickandeat.shared.enums.RoleName;

import java.util.Date;

public class Account {
    private final Long id;
    private String lastName;
    private String firstName;
    private final RoleName role;
    private AccountPhoneNumber accountPhoneNumber;
    private final String accountEmail;
    private final AccountBirthDate accountBirthDate;
    private final Date accountCreatedDate;
    private final Date accountUpdatedDate;
    private final AccountProInformations accountProInformations;



    public Account(Long id, String lastName, String firstName, RoleName role, String accountPhoneNumber, String accountEmail, String accountBirthDate, Date accountCreatedDate, Date accountUpdatedDate, AccountProInformations accountProInformations) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
        this.accountPhoneNumber = new AccountPhoneNumber(accountPhoneNumber);
        this.accountEmail = accountEmail;
        this.accountBirthDate = new AccountBirthDate(accountBirthDate);
        this.accountCreatedDate = accountCreatedDate;
        this.accountUpdatedDate = accountUpdatedDate;
        this.accountProInformations = accountProInformations;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public RoleName getRole() {
        return role;
    }

    public AccountPhoneNumber getAccountPhoneNumber() {
        return accountPhoneNumber;
    }

    public void setAccountPhoneNumber(AccountPhoneNumber accountPhoneNumber) {
        this.accountPhoneNumber = accountPhoneNumber;
    }

    public AccountBirthDate getAccountBirthDate() {
        return accountBirthDate;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public Date getAccountCreatedDate() {
        return accountCreatedDate;
    }

    public Date getAccountUpdatedDate() {
        return accountUpdatedDate;
    }

    public AccountProInformations getAccountProInformations() {
        return accountProInformations;
    }
}
