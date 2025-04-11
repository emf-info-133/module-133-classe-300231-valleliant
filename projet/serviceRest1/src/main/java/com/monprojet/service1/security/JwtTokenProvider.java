package com.monprojet.service1.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.monprojet.service1.dto.UserDTO;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey() {
        // Assure-toi que la clé est d'une taille suffisante pour HS512
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        
        // Si la clé est trop courte, on la complète à 512 bits
        if (keyBytes.length < 64) {
            // On utilise un padding ou on génère une nouvelle clé (ex. en remplissant avec des caractères répétés)
            String paddedSecret = String.format("%-64s", secret).replace(' ', '0'); // Compléter jusqu'à 512 bits (64 caractères)
            keyBytes = paddedSecret.getBytes(StandardCharsets.UTF_8);
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDTO userDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDTO.getId());
        claims.put("name", userDTO.getName());
        claims.put("email", userDTO.getEmail());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDTO.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
}
