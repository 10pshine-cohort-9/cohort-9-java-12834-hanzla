package com.tenpearls.contactmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        CookieCsrfTokenRepository csrfTokenRepository =
                CookieCsrfTokenRepository.withHttpOnlyFalse();

        http

                /*
                 * CSRF protection remains enabled.
                 *
                 * Login and registration are excluded because
                 * the user is not authenticated yet.
                 *
                 * Contact POST/PUT/PATCH/DELETE requests remain
                 * protected by CSRF.
                 */
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository)
                        .ignoringRequestMatchers(
                                "/api/v1/auth/login",
                                "/api/v1/auth/register"
                        )
                )

                /*
                 * Enable CORS.
                 */
                .cors(cors -> {})

                /*
                 * HTTP session is required because the application
                 * uses session-based authentication.
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED
                        )
                )

                /*
                 * Persist SecurityContext in the HTTP session.
                 */
                .securityContext(securityContext -> securityContext
                        .requireExplicitSave(false)
                )

                /*
                 * Authorization rules.
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * Public authentication endpoints.
                         */
                        .requestMatchers(
                                "/api/v1/auth/register",
                                "/api/v1/auth/login",
                                "/api/v1/auth/csrf"
                        ).permitAll()

                        /*
                         * Swagger / OpenAPI.
                         */
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        /*
                         * Everything else requires authentication.
                         */
                        .anyRequest().authenticated()
                )

                /*
                 * Spring Security owns the logout endpoint.
                 *
                 * Return HTTP 200 instead of redirecting because
                 * this is a REST API.
                 */
                .logout(logout -> logout

                        .logoutUrl("/api/v1/auth/logout")

                        .invalidateHttpSession(true)

                        .clearAuthentication(true)

                        .deleteCookies(
                                "JSESSIONID",
                                "XSRF-TOKEN"
                        )

                        .logoutSuccessHandler(
                                new HttpStatusReturningLogoutSuccessHandler()
                        )
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of(
                        "Content-Type",
                        "X-XSRF-TOKEN",
                        "X-Requested-With"
                )
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}