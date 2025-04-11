package com.monprojet.apigateway.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.JwtException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtils {

    // Méthode pour valider le token JWT
    public static boolean validateToken(String token, String secret) {
        try {
            Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token); // Si aucune exception n'est levée, le token est valide
            return true;
        } catch (ExpiredJwtException e) {
            // Le token est expiré
            return false;
        } catch (MalformedJwtException e) {
            // Le token est mal formé
            return false;
        } catch (UnsupportedJwtException e) {
            // Le token n'est pas supporté
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            // Autres erreurs, le token est invalide
            return false;
        }
    }

    // Méthode pour extraire le nom d'utilisateur (ou autre information) du JWT
    public static String getUsernameFromToken(String token, String secret) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();  // Extraire les informations du corps du token
            return claims.getSubject(); // Retourne le nom d'utilisateur (ou subject)
        } catch (JwtException | IllegalArgumentException e) {
            return null; // Si le token est invalide, retourne null
        }
    }

    // Méthode pour vérifier si le token est expiré
    public static boolean isTokenExpired(String token, String secret) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date()); // Vérifie si la date d'expiration est dans le passé
        } catch (JwtException | IllegalArgumentException e) {
            return true; // Si le token est invalide ou autre erreur, considérer comme expiré
        }
    }
}
