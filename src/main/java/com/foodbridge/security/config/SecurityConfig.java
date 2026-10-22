package com.foodbridge.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.foodbridge.security.filter.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // =================================================
                // PUBLIC ENDPOINTS
                // =================================================

                .requestMatchers(
                    "/api/users/register",
                    "/api/auth/login",
                    "/api/auth/verify-otp",

                    // Swagger UI
                    "/swagger-ui.html",
                    "/swagger-ui/**",

                    // OpenAPI documentation
                    "/v3/api-docs",
                    "/v3/api-docs/**",

                    // WebSocket
                    "/ws"
                ).permitAll()

                // =================================================
                // ADMIN ENDPOINTS
                // =================================================

                .requestMatchers(
                    "/api/admin/**"
                ).hasRole("ADMIN")

                // =================================================
                // DONATION CREATION
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/donations"
                ).hasRole("DONOR")

                // =================================================
                // DONATION CANCELLATION
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/donations/*/cancel"
                ).hasRole("DONOR")

                // =================================================
                // DONOR HISTORY
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/donations/my"
                ).hasRole("DONOR")

                // =================================================
                // VIEW AVAILABLE DONATIONS
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/donations"
                ).authenticated()

                // =================================================
                // ACCEPT / CLAIM
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/donations/*/accept"
                ).hasAnyRole("RECEIVER", "NGO")

                // =================================================
                // CLAIMED HISTORY
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/donations/claimed"
                ).hasAnyRole("RECEIVER", "NGO")

                // =================================================
                // COMPLETE DONATION
                // =================================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/donations/*/complete"
                ).hasAnyRole("RECEIVER", "NGO")

                // =================================================
                // COMPLETED HISTORY
                // =================================================

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/donations/completed"
                ).hasAnyRole("RECEIVER", "NGO")

                // =================================================
                // EVERYTHING ELSE
                // =================================================

                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}