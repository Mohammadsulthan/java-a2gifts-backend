package com.example.demo.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct; // Note: Use javax.annotation.PostConstruct if using older Spring Boot
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            // Check if Firebase is already initialized to prevent crashes on reboot
            if (FirebaseApp.getApps().isEmpty()) {
                
                // Read the secret key we pasted into Render's Environment Variables
                String firebaseCredentials = System.getenv("FIREBASE_CREDENTIALS");
                
                if (firebaseCredentials == null) {
                    System.err.println("ERROR: FIREBASE_CREDENTIALS environment variable is missing!");
                    return;
                }

                // Convert the string back into a stream that Firebase can read
                InputStream serviceAccount = new ByteArrayInputStream(firebaseCredentials.getBytes(StandardCharsets.UTF_8));

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("Firebase has been successfully initialized!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}