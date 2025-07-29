package com.example.csrf.csrf_example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user-related operations.
 * Handles protected and public endpoints.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    /**
     * Retrieves user profile data (protected endpoint).
     */
    @GetMapping("/user/profile")
    public ResponseEntity<String> getUserProfile() {
        return ResponseEntity.ok("User profile data");
    }

    /**
     * Updates user profile (protected endpoint, requires CSRF token).
     */
    @PostMapping("/user/profile")
    public ResponseEntity<String> updateUserProfile(@RequestBody String profileData) {
        // In a real application, process profileData
        return ResponseEntity.ok("Profile updated successfully");
    }

    /**
     * Retrieves public information (no CSRF required).
     */
    @GetMapping("/public/info")
    public ResponseEntity<String> getPublicInfo() {
        return ResponseEntity.ok("This is public information - no CSRF required");
    }
}