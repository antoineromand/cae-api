package com.pickandeat.authentication.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.pickandeat.authentication.infrastructure.config.jwt.SecurityJwtConfig;

@Configuration
@EnableConfigurationProperties(SecurityJwtConfig.class)
public class JwtBeanConfig {
}
