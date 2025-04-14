package com.monprojet.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.monprojet.apigateway.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/register")
@Tag(name = "Inscription", description = "Endpoint pour créer un compte utilisateur")
public class RegisterController {

    private final RestTemplate restTemplate;
    private final String serviceUrl = "http://service-rest1:8080/register";

    @Autowired
    public RegisterController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;

        // Getters / Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    @PostMapping
    @Operation(summary = "Créer un compte utilisateur", description = "Inscrit un nouvel utilisateur avec hash du mot de passe")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest registerRequest) {
        // Construire le corps de la requête avec RestTemplate
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> requestEntity = new HttpEntity<>(registerRequest, headers);

        try {
            // Effectuer l'appel HTTP POST vers le service
            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    serviceUrl,
                    HttpMethod.POST,
                    requestEntity,
                    UserDTO.class
            );

            return response; // Retourne la réponse obtenue du service REST1
        } catch (Exception e) {
            // Si une erreur survient, on retourne un BAD_GATEWAY
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
        }
    }
}
