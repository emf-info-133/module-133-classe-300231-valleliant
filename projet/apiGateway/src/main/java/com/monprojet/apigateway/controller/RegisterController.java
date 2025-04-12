package com.monprojet.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.monprojet.apigateway.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/register")
@Tag(name = "Inscription", description = "Endpoint pour créer un compte utilisateur")
public class RegisterController {

    private final WebClient webClient;
    private final String serviceUrl = "http://service-rest1:8080/register";

    @Autowired
    public RegisterController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
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
    public Mono<ResponseEntity<UserDTO>> register(@RequestBody RegisterRequest registerRequest) {
        // Construire le payload de la requête
        return webClient.post()
                .uri(serviceUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerRequest)
                .retrieve()
                .toEntity(UserDTO.class)
                .onErrorResume(error -> Mono.just(
                    ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(null)
                ));
    }
}
