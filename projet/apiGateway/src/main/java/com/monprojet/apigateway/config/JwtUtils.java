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

    public static Claims parseClaims(String token, String secret) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public static String getUsernameFromToken(String token, String secret) {
        Claims claims = parseClaims(token, secret);
        return claims != null ? claims.getSubject() : null;
    }

    public static boolean isTokenExpired(Claims claims) {
        Date expirationDate = claims.getExpiration();
        return expirationDate != null && expirationDate.before(new Date());
    }

    public static boolean validateToken(String token, String secret) {
        Claims claims = parseClaims(token, secret);
        return claims != null && !isTokenExpired(claims);
    }
}
