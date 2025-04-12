package com.monprojet.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentification", description = "Endpoints pour login et logout")
public class LoginController {

    private final WebClient webClient;
    private final String serviceBaseUrl = "http://service-rest1:8080/auth";

    @Autowired
    public LoginController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public static class LoginRequest {
        private String identifier;
        private String password;

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
    public Mono<ResponseEntity<String>> login(@RequestBody LoginRequest loginRequest) {
        return webClient.post()
                .uri(serviceBaseUrl + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .retrieve()
                .toEntity(String.class)
                .onErrorResume(error -> Mono.just(
                        ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                                .body("Erreur d'authentification : " + error.getMessage())
                ));
    }

    @PostMapping("/logout")
    @Operation(summary = "Déconnecter un utilisateur", description = "Transmet la requête de déconnexion au service backend")
    public Mono<ResponseEntity<String>> logout() {
        return webClient.post()
                .uri(serviceBaseUrl + "/logout")
                .retrieve()
                .toEntity(String.class)
                .onErrorResume(error -> Mono.just(
                        ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                                .body("Erreur de déconnexion : " + error.getMessage())
                ));
    }
}
