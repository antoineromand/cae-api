package com.pickandeat.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(FormLoginConfigurer::disable);
        httpSecurity.authorizeHttpRequests(
                (e) -> e.requestMatchers("/public/**").permitAll().requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger.html",
                        "/swagger-ui.html").permitAll()
                        .requestMatchers("/private/**").authenticated());
        return httpSecurity.build();
    }
}
