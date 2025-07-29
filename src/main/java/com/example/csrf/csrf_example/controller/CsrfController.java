package com.example.csrf.csrf_example.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling CSRF token requests.
 */
@RestController
@RequestMapping("/api")
public class CsrfController {

    /**
     * Returns the current CSRF token.
     * Useful for debugging or manual token retrieval.
     */
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(CsrfToken token) {
        return token;
    }
}