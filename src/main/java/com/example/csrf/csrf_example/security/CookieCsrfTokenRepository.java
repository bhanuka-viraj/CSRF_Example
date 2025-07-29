package com.example.csrf.csrf_example.security;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Custom CSRF token repository that stores tokens in a secure cookie.
 * The token is accessible by JavaScript for inclusion in AJAX requests.
 */
public class CookieCsrfTokenRepository implements CsrfTokenRepository {
    private static final String DEFAULT_CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";
    private static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";

    /**
     * Generates a new CSRF token using a random UUID.
     */
    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return new DefaultCsrfToken(DEFAULT_CSRF_HEADER_NAME, DEFAULT_CSRF_PARAMETER_NAME, UUID.randomUUID().toString());
    }

    /**
     * Saves the CSRF token in a cookie.
     * Sets secure flag based on request protocol and makes cookie accessible to JavaScript.
     */
    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        String tokenValue = token == null ? "" : token.getToken();
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie(DEFAULT_CSRF_COOKIE_NAME, tokenValue);
        cookie.setSecure(request.isSecure()); // Secure flag for HTTPS
        cookie.setPath("/"); // Cookie accessible across the application
        cookie.setHttpOnly(false); // Allow JavaScript access
        cookie.setMaxAge(token != null ? -1 : 0); // Session cookie or expire if token is null
        response.addCookie(cookie);
    }

    /**
     * Loads the CSRF token from the cookie.
     */
    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (jakarta.servlet.http.Cookie cookie : cookies) {
            if (DEFAULT_CSRF_COOKIE_NAME.equals(cookie.getName()) && StringUtils.hasText(cookie.getValue())) {
                return new DefaultCsrfToken(DEFAULT_CSRF_HEADER_NAME, DEFAULT_CSRF_PARAMETER_NAME, cookie.getValue());
            }
        }
        return null;
    }
}