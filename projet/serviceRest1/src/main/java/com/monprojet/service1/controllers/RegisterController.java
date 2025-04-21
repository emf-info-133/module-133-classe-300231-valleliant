package com.monprojet.service1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.monprojet.service1.dto.UserDTO;
import com.monprojet.service1.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Contrôleur qui gère l'inscription des utilisateurs.
 * Il permet de créer un compte utilisateur en hashant le mot de passe et en
 * validant les données de l'utilisateur.
 */
@RestController
@RequestMapping("/register")
@Tag(name = "Inscription", description = "Endpoint pour créer un compte utilisateur")
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * DTO représentant la requête d'inscription de l'utilisateur.
     * Il contient les informations nécessaires pour créer un nouvel utilisateur.
     */
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;

        // Getters et Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * Endpoint pour créer un compte utilisateur.
     * Il valide les champs d'inscription, crée un utilisateur et retourne une
     * réponse appropriée.
     * 
     * @param registerRequest L'objet contenant les informations d'inscription de
     *                        l'utilisateur.
     * @return Une réponse HTTP avec l'état de la création de l'utilisateur :
     *         - 201 CREATED si l'utilisateur est créé avec succès.
     *         - 400 BAD REQUEST si les champs sont invalides ou manquants.
     */
    @PostMapping
    @Operation(summary = "Créer un compte utilisateur", description = "Inscrit un nouvel utilisateur en hashant le mot de passe")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest) {
        // Validation des champs
        if (registerRequest.getName() == null || registerRequest.getEmail() == null
                || registerRequest.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Le nom, l'email et le mot de passe sont obligatoires.");
        }

        // Création du UserDTO à partir de la requête
        UserDTO userDTO = new UserDTO(null, registerRequest.getName(), registerRequest.getEmail(), false);

        try {
            // Création de l'utilisateur dans la base de données
            UserDTO createdUser = userService.createUser(userDTO, registerRequest.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            // Gestion des exceptions de validation
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
