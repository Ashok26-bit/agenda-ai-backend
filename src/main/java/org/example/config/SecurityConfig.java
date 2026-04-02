package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // 1. CSRF-a thookiduvom (POST work aaga)
                .cors(cors -> cors.configure(http))    // 2. CORS allow pannuvom (Flutter-ku)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // 3. Ellaa API-kum (Submit/Report) permission tharuvom
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}