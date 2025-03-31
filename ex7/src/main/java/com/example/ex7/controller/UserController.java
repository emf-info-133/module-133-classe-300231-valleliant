package com.example.ex7.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpSession session, @RequestParam String username,
            @RequestParam String password) {
        // Vérification simple des identifiants (ne pas utiliser en production)
        if ("user".equals(username) && "pass".equals(password)) {
            session.setAttribute("username", username);
            session.setAttribute("visites", 0);
            return ResponseEntity.ok("Connecté");
        } else {
            return ResponseEntity.badRequest().body("Échec de la connexion");
        }
    }

    @GetMapping("/visites")
    public ResponseEntity<String> visites(HttpSession session) {
        if (session.getAttribute("username") != null) {
            Integer visites = (Integer) session.getAttribute("visites");
            if (visites == null) {
                visites = 0;
            }
            visites++;
            session.setAttribute("visites", visites);
            return ResponseEntity.ok("Nombre de visites: " + visites);
        } else {
            return ResponseEntity.badRequest().body("Non connecté");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Déconnecté");
    }
}