package com.pickandeat.account.domain;

import com.pickandeat.shared.enums.RoleName;

import java.util.Date;
import java.util.UUID;

public class Account {
    private final Long id;
    private String lastName;
    private String firstName;
    private final RoleName role;
    private AccountPhoneNumber accountPhoneNumber;
    private String accountEmail;
    private final AccountBirthDate accountBirthDate;
    private Date accountCreatedDate;
    private Date accountUpdatedDate;
    private AccountProInformations accountProInformations;

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
}
