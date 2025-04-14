package com.monprojet.apigateway.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import reactor.core.publisher.Mono;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Order(-1)
@Component
public class JwtAuthFilter implements WebFilter {

    @Value("${jwt.secret}")
    private String jwtSecret; // Clé secrète utilisée pour valider le JWT

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        // Si la requête est pour l'authentification ou l'enregistrement, ne pas passer par le filtre JWT
        if (path.startsWith("/auth") || path.startsWith("/register")) {
            return chain.filter(exchange);
        }

        // Extraction du token JWT
        String token = extractToken(exchange);

        if (token == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token JWT manquant"));
        }

        // Vérification de la validité du token
        if (!isValidToken(token)) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token JWT invalide ou expiré"));
        }

        // Extraction du nom d'utilisateur du token
        String username = extractUsernameFromToken(token);

        if (username == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nom d'utilisateur introuvable dans le token"));
        }

        // Vérifie si l'utilisateur est connecté
        if (!isUserLoggedIn(username)) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non connecté"));
        }

        // Création de l'authentification pour le contexte de sécurité
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")) // 👈 ajoute un rôle par défaut
        );

        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Retire le "Bearer " du début du token
        }
        return null;
    }

    private boolean isValidToken(String token) {
        try {
            Claims claims = JwtUtils.parseClaims(token, jwtSecret);
            // Vérifie si le token n'est pas expiré
            return claims != null && !JwtUtils.isTokenExpired(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private String extractUsernameFromToken(String token) {
        // Extraire le nom d'utilisateur (ou d'autres informations) du token JWT
        return JwtUtils.getUsernameFromToken(token, jwtSecret);
    }

    // Vérifie si l'utilisateur est connecté
    private boolean isUserLoggedIn(String username) {
        // Cette méthode pourrait être étendue pour vérifier les utilisateurs connectés dans une base de données
        return true;  // Supposons que l'utilisateur est connecté
    }
}
