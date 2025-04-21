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

/**
 * Contrôleur qui gère l'authentification des utilisateurs (connexion et
 * déconnexion).
 * Il permet aux utilisateurs de se connecter et de se déconnecter en utilisant
 * des sessions HTTP.
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Méthode pour connecter un utilisateur.
     * Elle tente d'authentifier l'utilisateur en utilisant les informations de
     * connexion fournies.
     * Si l'utilisateur est déjà connecté (basé sur la session), une réponse
     * appropriée est renvoyée.
     * 
     * @param loginRequest L'objet contenant le nom d'utilisateur et le mot de
     *                     passe.
     * @param request      La requête HTTP qui contient la session.
     * @return Un message indiquant si la connexion a réussi ou échoué.
     */
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        // Vérifie si l'utilisateur est déjà connecté en vérifiant la session
        HttpSession session = request.getSession(false); // false signifie ne pas créer une nouvelle session si elle
                                                         // n'existe pas déjà
        if (session != null && session.getAttribute("username") != null) {
            // Si une session existe déjà, renvoie un message indiquant que l'utilisateur
            // est déjà connecté
            return "Utilisateur déjà connecté";
        }

        try {
            // Tentative d'authentification avec les informations fournies dans le corps de
            // la requête
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authResult = authenticationManager.authenticate(authToken);

            // Enregistrer l'authentification dans le contexte de sécurité
            SecurityContextHolder.getContext().setAuthentication(authResult);

            // Créer une session pour l'utilisateur authentifié
            HttpSession newSession = request.getSession(true); // true crée une nouvelle session si nécessaire
            newSession.setAttribute("username", loginRequest.getUsername()); // Stocke l'utilisateur dans la session

            return "Login réussi";
        } catch (Exception ex) {
            return "Erreur d'authentification : " + ex.getMessage();
        }
    }

    /**
     * Méthode pour déconnecter un utilisateur.
     * Elle invalide la session existante et déconnecte l'utilisateur.
     * 
     * @param request  La requête HTTP qui contient la session.
     * @param response La réponse HTTP.
     * @return Un message indiquant si la déconnexion a réussi.
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidating the session
        HttpSession session = request.getSession(false); // false signifie que cela ne crée pas une nouvelle session
        if (session != null) {
            session.invalidate(); // Invalider la session existante
        }

        return "Déconnexion réussie";
    }

    /**
     * DTO représentant la requête de connexion.
     * Elle contient les informations nécessaires pour authentifier un utilisateur :
     * nom d'utilisateur et mot de passe.
     */
    public static class LoginRequest {
        private String username;
        private String password;

        // Getters et setters
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
