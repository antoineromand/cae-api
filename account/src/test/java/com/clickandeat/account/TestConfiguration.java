package com.clickandeat.account;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@ComponentScan(basePackages = "com.clickandeat.account")
@EntityScan("com.clickandeat.account.infrastructure.model")
@EnableJpaRepositories("com.clickandeat.account.infrastructure.repository")
public class TestConfiguration {}
