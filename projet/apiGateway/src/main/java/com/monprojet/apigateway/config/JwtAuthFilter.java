package com.monprojet.apigateway.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
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
        String path = exchange.getRequest().getPath().toString();

        if (path.startsWith("/auth") || path.startsWith("/register")) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange);

        System.out.println("Token reçu: " + token);

        if (token == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token JWT manquant"));
        }

        if (!isValidToken(token)) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token JWT invalide ou expiré"));
        }

        String username = extractUsernameFromToken(token);

        if (username == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Nom d'utilisateur introuvable dans le token"));
        }

        System.out.println("Nom d'utilisateur extrait: " + username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
                null);
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }

    private String extractToken(ServerWebExchange exchange) {
        // Vérifie si l'en-tête Authorization existe et contient un token de type Bearer
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Extraire le token après "Bearer "
        }
        return null;
    }

    private boolean isValidToken(String token) {
        try {
            // Utiliser la méthode getSigningKey de JwtUtils
            Claims claims = Jwts.parser()
                    .setSigningKey(JwtUtils.getSigningKey(jwtSecret)) // Appeler la méthode de JwtUtils
                    .parseClaimsJws(token)
                    .getBody();

            // Vous pouvez vérifier l'expiration ici si vous le souhaitez
            Date expirationDate = claims.getExpiration();
            if (expirationDate.before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private String extractUsernameFromToken(String token) {
        // Extraire le nom d'utilisateur (ou d'autres informations) du token JWT
        return JwtUtils.getUsernameFromToken(token, jwtSecret);
    }
}
