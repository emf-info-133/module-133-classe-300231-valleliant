package com.monprojet.apigateway.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtils {

    public static SecretKey getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    
        // Complète la clé jusqu’à 64 octets si elle est trop courte pour HS512
        if (keyBytes.length < 64) {
            String paddedSecret = String.format("%-64s", secret).replace(' ', '0');
            keyBytes = paddedSecret.getBytes(StandardCharsets.UTF_8);
        }
    
        return Keys.hmacShaKeyFor(keyBytes);
    }
    

    public static boolean validateToken(String token, String secret) {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey(secret)) // Appel à la méthode de génération de clé
                .parseClaimsJws(token)
                .getBody();
    
            // Vérification de l'expiration
            if (claims.getExpiration().before(new Date())) {
                return false; // Token expiré
            }
    
            return true; // Token valide
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token invalide ou malformé
        }
    }
    
    

    public static String getUsernameFromToken(String token, String secret) {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey(secret)) // ✅ cohérent aussi ici
                .parseClaimsJws(token)
                .getBody();

            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Erreur extraction subject : " + e.getMessage());
            return null;
        }
    }

    public static boolean isTokenExpired(String token, String secret) {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey(secret))
                .parseClaimsJws(token)
                .getBody();

            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid or malformed, treat as expired
            return true;
        }
    }
}
