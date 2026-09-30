package com.example.dfbbackend.config;

import com.example.dfbbackend.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;


    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;

    }


    // =============================================================
    // PASSWORD ENCODER
    // =============================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }


    // =============================================================
    // CORS CONFIGURATION
    // =============================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();


        configuration.setAllowedOrigins(
                Arrays.stream(allowedOrigins.split(","))
                        .map(String::trim)
                        .filter(origin -> !origin.isEmpty())
                        .toList()
        );


        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );


        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type",
                        "Accept"
                )
        );


        configuration.setAllowCredentials(false);


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration(
                "/**",
                configuration
        );


        return source;

    }


    // =============================================================
    // SECURITY FILTER CHAIN
    // =============================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {


        http

                // =================================================
                // CSRF
                // =================================================

                .csrf(
                        csrf -> csrf.disable()
                )


                // =================================================
                // CORS
                // =================================================

                .cors(
                        cors ->
                                cors.configurationSource(
                                        corsConfigurationSource()
                                )
                )


                // =================================================
                // SESSION MANAGEMENT
                // =================================================

                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )


                // =================================================
                // AUTHORIZATION RULES
                // =================================================

                .authorizeHttpRequests(
                        auth -> auth


                                // =================================
                                // SWAGGER / OPENAPI
                                //
                                // Swagger UI and OpenAPI JSON
                                // are public documentation resources.
                                // =================================

                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**"
                                )
                                .permitAll()


                                // =================================
                                // USER AUTH APIs
                                // =================================

                                .requestMatchers(
                                        "/api/auth/**"
                                )
                                .permitAll()


                                // =================================
                                // ADMIN AUTH APIs
                                // =================================

                                .requestMatchers(
                                        "/api/admin/auth/**"
                                )
                                .permitAll()


                                // =================================
                                // CORS PREFLIGHT
                                // =================================

                                .requestMatchers(
                                        HttpMethod.OPTIONS,
                                        "/**"
                                )
                                .permitAll()


                                // =================================
                                // TEST API
                                // =================================

                                .requestMatchers(
                                        "/api/test"
                                )
                                .authenticated()


                                // =================================
                                // DOMAIN READ APIs
                                //
                                // USER + ADMIN
                                // =================================

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/domains/**"
                                )
                                .authenticated()


                                // =================================
                                // DOMAIN CREATE
                                //
                                // ADMIN ONLY
                                // =================================

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/domains/**"
                                )
                                .hasRole("ADMIN")


                                // =================================
                                // DOMAIN UPDATE
                                //
                                // ADMIN ONLY
                                // =================================

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/domains/**"
                                )
                                .hasRole("ADMIN")


                                // =================================
                                // DOMAIN ACTIVATE / DEACTIVATE
                                //
                                // ADMIN ONLY
                                // =================================

                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "/api/domains/**"
                                )
                                .hasRole("ADMIN")


                                // =================================
                                // VISIT TYPE READ APIs
                                //
                                // USER + ADMIN
                                // =================================

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/domains/*/visit-types"
                                )
                                .authenticated()


                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/visit-types/**"
                                )
                                .authenticated()


                                // =================================
                                // VISIT TYPE CREATE
                                //
                                // ADMIN ONLY
                                // =================================

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/domains/*/visit-types"
                                )
                                .hasRole("ADMIN")


                                // =================================
                                // VISIT TYPE UPDATE
                                //
                                // ADMIN ONLY
                                // =================================

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/visit-types/**"
                                )
                                .hasRole("ADMIN")


                                // =================================
                                // VISIT TYPE ACTIVATE /
                                // DEACTIVATE
                                //
                                // ADMIN ONLY
                                // =================================

                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "/api/visit-types/**"
                                )
                                .hasRole("ADMIN")


                                // =================================
                                // VISIT TYPE PERMANENT DELETE
                                //
                                // ADMIN ONLY
                                // =================================

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/visit-types/**"
                                )
                                .hasRole("ADMIN")


                                // =================================
                                // ADMIN APIs
                                // =================================

                                .requestMatchers(
                                        "/api/admin/**"
                                )
                                .hasRole("ADMIN")


                                // =================================
                                // EVERYTHING ELSE
                                // =================================

                                .anyRequest()
                                .authenticated()
                )


                // =================================================
                // UNAUTHORIZED RESPONSE
                // =================================================

                .exceptionHandling(
                        exception ->
                                exception.authenticationEntryPoint(
                                        (
                                                request,
                                                response,
                                                authException
                                        ) ->

                                                response.sendError(
                                                        HttpServletResponse.SC_UNAUTHORIZED,
                                                        "Unauthorized"
                                                )
                                )
                )


                // =================================================
                // JWT AUTHENTICATION FILTER
                // =================================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();

    }


    // =============================================================
    // AUTHENTICATION MANAGER
    // =============================================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration
                .getAuthenticationManager();

    }

}