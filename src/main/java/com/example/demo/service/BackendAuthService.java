package com.example.demo.service;

import java.io.FileInputStream;
import java.io.IOException;
//
//import javax.annotation.PostConstruct;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;

//package com.yourpackage.name; // Change this to your actual package name!

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

@Service
public class BackendAuthService {

    public BackendAuthService() {
        System.out.println("BackendAuthService bean created");
    }
    // @PostConstruct tells Spring Boot to run this method exactly once when the server starts
    @PostConstruct
    public void initializeFirebase() {
        try {
            // Points to the private key file in your resources folder
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // Prevents initializing the app multiple times
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK initialized successfully.");
            }
        } catch (IOException e) {
            System.err.println("CRITICAL ERROR: Could not find or read serviceAccountKey.json!");
            e.printStackTrace();
        }
    }

    // Takes the token, verifies it, and returns the user's email if valid
    public String verifyUser(String idTokenFromFrontend) {
        try {
        	System.out.println("function call correct:");
            // This validates the mathematical signature and expiration date with Google
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idTokenFromFrontend);
            
            // Success! Return the email
            return decodedToken.getEmail(); 
        } catch (Exception e) {
            // Token is fake, expired, or tampered with
            return null; 
        }
    }
}
