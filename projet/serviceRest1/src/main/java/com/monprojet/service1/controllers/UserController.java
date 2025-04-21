package com.monprojet.service1.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.monprojet.service1.dto.UserDTO;
import com.monprojet.service1.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Contrôleur qui gère les requêtes HTTP liées aux utilisateurs. Il permet de
 * récupérer, créer,
 * mettre à jour et rechercher des utilisateurs via l'API REST.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateurs", description = "API pour gérer les utilisateurs")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Récupère tous les utilisateurs.
     * 
     * @return Une réponse HTTP contenant la liste de tous les utilisateurs.
     */
    @GetMapping
    @Operation(summary = "Récupérer tous les utilisateurs", description = "Renvoie la liste de tous les utilisateurs")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Récupère un utilisateur par son identifiant.
     * 
     * @param id L'identifiant de l'utilisateur à récupérer.
     * @return Une réponse HTTP contenant les détails de l'utilisateur si trouvé,
     *         sinon un code HTTP 404 (Not Found).
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par ID", description = "Renvoie un utilisateur spécifique par son ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Récupère un utilisateur par son nom.
     * 
     * @param name Le nom de l'utilisateur à rechercher.
     * @return Une réponse HTTP contenant les détails de l'utilisateur si trouvé,
     *         sinon un code HTTP 404 (Not Found).
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<UserDTO> getUserByName(@PathVariable String name) {
        UserDTO user = userService.getUserByName(name);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * Récupère un utilisateur par son adresse email.
     * 
     * @param email L'adresse email de l'utilisateur à rechercher.
     * @return Une réponse HTTP contenant les détails de l'utilisateur si trouvé,
     *         sinon un code HTTP 404 (Not Found).
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * Crée un nouvel utilisateur.
     * 
     * @param payload Un map contenant les informations de l'utilisateur (nom,
     *                email, mot de passe).
     * @return Une réponse HTTP contenant les détails de l'utilisateur créé.
     */
    @PostMapping
    @Operation(summary = "Créer un nouvel utilisateur", description = "Crée un nouvel utilisateur et renvoie les détails")
    public ResponseEntity<UserDTO> createUser(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String email = payload.get("email");
        String password = payload.get("password");

        UserDTO userDTO = new UserDTO(null, name, email, false);
        UserDTO createdUser = userService.createUser(userDTO, password);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Met à jour un utilisateur existant par son identifiant.
     * 
     * @param id      L'identifiant de l'utilisateur à mettre à jour.
     * @param payload Un map contenant les nouvelles informations de l'utilisateur
     *                (nom, email).
     * @return Une réponse HTTP contenant les détails de l'utilisateur mis à jour,
     *         ou un code d'erreur en cas de conflit.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un utilisateur", description = "Met à jour un utilisateur existant par son ID")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Integer id,
            @RequestBody Map<String, String> payload) {

        String name = payload.get("name");
        String email = payload.get("email");

        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Erreur si l'un des champs est vide
        }

        try {
            // Appel du service pour mettre à jour l'utilisateur
            UserDTO updatedUser = userService.updateUser(id, name, email);
            if (updatedUser != null) {
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // L'utilisateur n'a pas été trouvé
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Erreur en cas de doublon de nom ou email
        }
    }
}
