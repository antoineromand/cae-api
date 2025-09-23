package com.clickandeat.authentication.domain.service;

public interface IPasswordService {
  String hashPassword(String password);

  boolean matches(String raw, String hashed);
}
