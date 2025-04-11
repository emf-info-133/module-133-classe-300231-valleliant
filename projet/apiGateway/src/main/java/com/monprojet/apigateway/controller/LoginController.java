package com.monprojet.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentification", description = "Endpoints pour login et logout")
public class LoginController {

    private final RestTemplate restTemplate;
    private final String serviceUrl = "http://service-rest1:8082/auth/login"; // URL du service backend

    @Autowired
    public LoginController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static class LoginRequest {
        private String identifier;  // should match the backend's 'identifier'
        private String password;
    
        // Getters and Setters
        public String getIdentifier() {
            return identifier;
        }
    
        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }
    
        public String getPassword() {
            return password;
        }
    
        public void setPassword(String password) {
            this.password = password;
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Authentifier un utilisateur", description = "Transmet les identifiants au service backend pour authentification")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Transmettre la requête au service backend pour authentification
        ResponseEntity<String> response = restTemplate.exchange(
            serviceUrl,
            HttpMethod.POST,
            new HttpEntity<>(loginRequest),
            String.class
        );

        // Retourner la réponse du service backend
        return response;
    }

    @PostMapping("/logout")
    @Operation(summary = "Déconnecter un utilisateur", description = "Transmet la requête de déconnexion au service backend")
    public ResponseEntity<?> logout() {
        // URL du service backend pour la déconnexion
        String serviceLogoutUrl = "http://service-rest1:8082/auth/logout";
        ResponseEntity<String> response = restTemplate.exchange(
            serviceLogoutUrl,
            HttpMethod.POST,
            null,
            String.class
        );

        return response;
    }
}
