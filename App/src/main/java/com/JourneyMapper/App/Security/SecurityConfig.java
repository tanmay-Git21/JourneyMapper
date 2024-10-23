package com.JourneyMapper.App.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/register","/login").permitAll() // Allow unauthenticated access to /register
                .anyRequest().authenticated() // All other requests need authentication
            )
            .csrf().disable(); // Disable CSRF protection for non-browser clients

        return http.build();
    }
}
