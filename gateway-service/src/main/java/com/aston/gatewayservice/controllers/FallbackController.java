package com.aston.gatewayservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @GetMapping("/fallback/users")
    public ResponseEntity<String> userFallback() {
        return ResponseEntity.status(503)
                .body("User service is currently unavailable");
    }

    @GetMapping("/fallback/notification")
    public ResponseEntity<String> notificationFallback() {
        return ResponseEntity.status(503)
                .body("Notification service is currently unavailable");
    }
}
