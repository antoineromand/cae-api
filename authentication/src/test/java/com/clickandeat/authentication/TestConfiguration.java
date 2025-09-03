package com.clickandeat.authentication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.clickandeat.authentication")
@EntityScan("com.clickandeat.authentication.infrastructure.model")
@EnableJpaRepositories("com.clickandeat.authentication.infrastructure.repository")
public class TestConfiguration {

}
