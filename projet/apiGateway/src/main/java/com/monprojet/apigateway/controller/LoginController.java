package com.monprojet.apigateway.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String SERVICE_BASE_URL = "http://service-rest1:8080/auth";

    public static class LoginRequest {
        private String username;
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        // Check if the user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Utilisateur déjà connecté avec une session active");
        }

        // Envoyer la requête au microservice d'authentification
        ResponseEntity<String> response = restTemplate.postForEntity(
                SERVICE_BASE_URL + "/login", loginRequest, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // Crée la session si elle n'existe pas et associe l'utilisateur à la session
            session = request.getSession(true);
            session.setAttribute("user", loginRequest.getUsername()); // Sauvegarde l'info utilisateur
            return ResponseEntity.ok("Connexion réussie !");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Échec de la connexion : " + response.getBody());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Utilisateur non connecté, impossible de se déconnecter");
        }

        // Appel au service de logout distant (facultatif)
        restTemplate.postForEntity(SERVICE_BASE_URL + "/logout", null, String.class);

        // Invalidation de la session
        session.invalidate();
        return ResponseEntity.ok("Déconnexion réussie");
    }
}
