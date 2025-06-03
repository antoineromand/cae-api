package com.pickandeat.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.pickandeat.api",
		"com.pickandeat.authentication"
})
@EnableJpaRepositories(basePackages = "com.pickandeat.authentication.infrastructure.repository")
@EntityScan(basePackages = "com.pickandeat.authentication.infrastructure.model")
public class PickAndEatApplication {
	public static void main(String[] args) {
		SpringApplication.run(PickAndEatApplication.class, args);
	}
}
