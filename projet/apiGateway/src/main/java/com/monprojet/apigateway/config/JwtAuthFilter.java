package com.monprojet.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Component
public class JwtAuthFilter implements WebFilter {

    @Value("${jwt.secret}")
    private String jwtSecret; // Clé secrète utilisée pour valider le JWT

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange);

        if (token == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token JWT manquant"));
        }

        if (!isValidToken(token)) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token JWT invalide ou expiré"));
        }

        // Valider et extraire l'utilisateur du JWT
        String username = extractUsernameFromToken(token);

        // Ajouter l'utilisateur authentifié dans le contexte de sécurité
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)); // Authentifier l'utilisateur dans le contexte
    }

    private String extractToken(ServerWebExchange exchange) {
        // Vérifie si l'en-tête Authorization existe et contient un token de type Bearer
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);  // Extraire le token après "Bearer "
        }
        return null;
    }

    private boolean isValidToken(String token) {
        try {
            // Logique de validation du JWT (par exemple, utiliser la clé secrète pour valider)
            return JwtUtils.validateToken(token, jwtSecret);
        } catch (Exception e) {
            return false;
        }
    }

    private String extractUsernameFromToken(String token) {
        // Extraire le nom d'utilisateur (ou d'autres informations) du token JWT
        return JwtUtils.getUsernameFromToken(token, jwtSecret);
    }
}
