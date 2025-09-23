package com.clickandeat.authentication.infrastructure.config;

import com.clickandeat.authentication.infrastructure.config.jwt.SecurityJwtConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SecurityJwtConfig.class)
public class JwtBeanConfig {}
