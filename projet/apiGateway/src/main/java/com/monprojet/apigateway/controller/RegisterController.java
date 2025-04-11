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
    private final String serviceUrl = "http://service-rest1:8082/register";  // URL de l'API backend (service1)

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
        // Construire le payload de la requête
        UserDTO userDTO = new UserDTO(null, registerRequest.getName(), registerRequest.getEmail());

        // Faire appel à l'API backend pour créer l'utilisateur
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserDTO> request = new HttpEntity<>(userDTO, headers);
        
        // Appel REST vers service1 (le backend)
        ResponseEntity<UserDTO> response = restTemplate.exchange(serviceUrl, HttpMethod.POST, request, UserDTO.class);
        
        // Retourner la réponse du backend
        return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
    }
}
