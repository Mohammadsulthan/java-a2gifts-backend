package com.example.demo.Controller;

import com.example.demo.service.BackendAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SecureController {

    private final BackendAuthService authService;

    // Constructor Injection
    public SecureController(BackendAuthService authService) {
        this.authService = authService;
        System.out.println("Injected service = " + authService);
    }

    @PostMapping("/secure-endpoint")
    public ResponseEntity<String> handleSecureRequest(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody(required = false) String requestBody) {

        System.out.println("Inside handleSecureRequest");

        // Check Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Access Denied: Missing or invalid token.");
        }

        // Extract token
        String idToken = authHeader.substring(7);
        System.out.println("Token value: " + idToken);

        // Verify token
        String authenticatedUserEmail = authService.verifyUser(idToken);
        System.out.println("Authenticated user: " + authenticatedUserEmail);

        if (authenticatedUserEmail != null) {

            System.out.println("Valid request received from: " + authenticatedUserEmail);
            System.out.println("Payload from frontend: " + requestBody);

            return ResponseEntity.ok(
                    "Success! Java backend verified the identity of: "
                            + authenticatedUserEmail);
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Access Denied: Token is invalid or expired.");
    }
}