package com.example.csrf.csrf_example.config;

import com.example.csrf.csrf_example.security.CookieCsrfTokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

/**
 * Security configuration for the application.
 * Configures CSRF protection, authentication, and authorization.
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     * Enables CSRF protection with cookie-based repository and sets up login/logout handling.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(new CookieCsrfTokenRepository()) // Use cookie-based CSRF storage
                        .csrfTokenRequestHandler(requestHandler)
                        .ignoringRequestMatchers("/api/public/**") // Exempt public endpoints from CSRF
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/api/public/**").permitAll() // Public resources
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login") // Login endpoint
                        .defaultSuccessUrl("/index.html", true) // Redirect after successful login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Logout endpoint
                        .logoutSuccessUrl("/index.html?logout") // Redirect after logout
                        .invalidateHttpSession(true) // Invalidate session on logout
                        .deleteCookies("JSESSIONID", "XSRF-TOKEN") // Clear session and CSRF cookies
                        .permitAll()
                );

        return http.build();
    }
}