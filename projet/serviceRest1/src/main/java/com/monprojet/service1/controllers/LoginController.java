package com.monprojet.service1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import com.monprojet.service1.dto.UserDTO;
import com.monprojet.service1.models.User;
import com.monprojet.service1.repositories.UserRepository;
import com.monprojet.service1.security.InMemorySessionService;
import com.monprojet.service1.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentification", description = "Endpoints de login et logout")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InMemorySessionService sessionService;

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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            System.out.println("Tentative de login pour : " + loginRequest.getIdentifier());

            // Création du token d'authentification
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getIdentifier(), loginRequest.getPassword());

            // Tentative d'authentification
            Authentication authResult = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authResult);

            // Identification de l'utilisateur à partir du principal
            String principalIdentifier = ((org.springframework.security.core.userdetails.User) authResult
                    .getPrincipal()).getUsername();

            System.out.println("Principal authentifié : " + principalIdentifier);

            // Recherche de l'utilisateur par email ou nom
            User user = userRepository.findByEmailOrName(principalIdentifier, principalIdentifier)
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé en base"));

            // Création du DTO pour l'utilisateur
            UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getEmail());

            // Vérifie si une session existe déjà pour cet utilisateur (prévenir la
            // connexion simultanée)
            String token = jwtTokenProvider.generateToken(userDTO);

            if (sessionService.getSession(token) != null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Cet utilisateur est déjà connecté. Une seule session est autorisée.");
            }

            // Enregistrer la session en mémoire
            sessionService.createSession(token, userDTO);

            // Création de la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", userDTO);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            System.err.println("Erreur d'authentification : " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login échoué : Informations d'identification incorrectes.");
        } catch (Exception e) {
            System.err.println("Erreur serveur : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne : " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String token) {
        sessionService.removeSession(token); // Supprimer la session en mémoire
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Déconnexion réussie");
    }

}
