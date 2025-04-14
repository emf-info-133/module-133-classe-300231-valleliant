package com.monprojet.service1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        // Vérifie si l'utilisateur est déjà connecté en vérifiant la session
        HttpSession session = request.getSession(false); // false signifie ne pas créer une nouvelle session si elle n'existe pas déjà
        if (session != null && session.getAttribute("username") != null) {
            // Si une session existe déjà, renvoie un message indiquant que l'utilisateur est déjà connecté
            return "Utilisateur déjà connecté";
        }

        try {
            // Tentative d'authentification avec les informations fournies dans le corps de la requête
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authResult = authenticationManager.authenticate(authToken);

            // Enregistrer l'authentification dans le contexte de sécurité
            SecurityContextHolder.getContext().setAuthentication(authResult);

            // Créer une session pour l'utilisateur authentifié
            HttpSession newSession = request.getSession(true);  // true crée une nouvelle session si nécessaire
            newSession.setAttribute("username", loginRequest.getUsername());  // Stocke l'utilisateur dans la session

            return "Login réussi";
        } catch (Exception ex) {
            return "Erreur d'authentification : " + ex.getMessage();
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidating the session
        HttpSession session = request.getSession(false); // false signifie que cela ne crée pas une nouvelle session
        if (session != null) {
            session.invalidate();  // Invalider la session existante
        }

        return "Déconnexion réussie";
    }

    // Classe LoginRequest rendue statique
    public static class LoginRequest {
        private String username;
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
