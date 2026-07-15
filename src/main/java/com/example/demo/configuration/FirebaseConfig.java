package com.example.demo.configuration;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            // 1. Fetch the JSON string from the Environment Variable
            String firebaseCredentials = System.getenv("FIREBASE_CREDENTIALS");

            // Safety check
            if (firebaseCredentials == null || firebaseCredentials.isEmpty()) {
                throw new RuntimeException("CRITICAL ERROR: FIREBASE_CREDENTIALS environment variable is missing!");
            }

            // 2. Convert the text string back into a stream that Firebase can read
            InputStream credentialsStream = new ByteArrayInputStream(firebaseCredentials.getBytes(StandardCharsets.UTF_8));

            // 3. Initialize Firebase
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase securely initialized via Environment Variable!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}