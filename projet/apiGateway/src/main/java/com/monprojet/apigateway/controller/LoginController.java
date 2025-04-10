package com.monprojet.apigateway.controller;
 
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.AuthenticationException;

 
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentification", description = "Endpoints pour login et logout")
public class LoginController {
 
    private final AuthenticationManager authenticationManager;
    @Autowired
    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    public static class LoginRequest {
        private String email;
        private String password;
        // Getters et Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    @PostMapping("/login")
    @Operation(summary = "Authentifier un utilisateur", description = "Vérifie les identifiants et crée une session")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            Authentication authResult = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authResult);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            return ResponseEntity.ok("Login réussi");
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Login échoué : " + ex.getMessage());
        }
    }
    @PostMapping("/logout")
    @Operation(summary = "Déconnecter un utilisateur", description = "Invalide la session en cours")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Déconnexion réussie");
    }
}