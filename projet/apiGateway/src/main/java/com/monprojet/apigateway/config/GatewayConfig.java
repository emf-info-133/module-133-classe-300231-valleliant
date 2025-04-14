package com.monprojet.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

@Configuration
public class GatewayConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter(addAuthorizationHeader()); // Ajouter le filtre pour ajouter le token
    }

    // Fonction pour ajouter l'en-tête Authorization avec le token JWT
    private ExchangeFilterFunction addAuthorizationHeader() {
        return (request, next) -> {
            // Récupérer le token d'authentification (depuis la session ou SecurityContext)
            String token = getTokenFromSecurityContext();

            if (token != null && !token.isEmpty()) {
                // Ajouter l'en-tête Authorization à la requête
                request = ClientRequest.from(request)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build();
            }

            // Continue avec la requête
            return next.exchange(request);
        };
    }

    // Méthode pour récupérer le token depuis le SecurityContext de Spring Security
    private String getTokenFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Assumer que le token JWT est stocké dans l'objet d'authentification
        if (authentication != null && authentication.getCredentials() != null) {
            return (String) authentication.getCredentials();  // Le token peut être stocké dans les credentials
        }
        return null;
    }
}
