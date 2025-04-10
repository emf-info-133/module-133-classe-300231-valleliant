package com.monprojet.service1.controllers;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.monprojet.service1.dto.UserDTO;
import com.monprojet.service1.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
 
@RestController
@RequestMapping("/register")
@Tag(name = "Inscription", description = "Endpoint pour créer un compte utilisateur")
public class RegisterController {
 
    private final UserService userService;
    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }
    // DTO pour inscription
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        // Getters et Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    @PostMapping
    @Operation(summary = "Créer un compte utilisateur", description = "Inscrit un nouvel utilisateur en hashant le mot de passe")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest registerRequest) {
        // Créer un UserDTO sans mot de passe
        UserDTO userDTO = new UserDTO(null, registerRequest.getName(), registerRequest.getEmail());
        // Le service se charge du hashage et de la création en BDD
        UserDTO createdUser = userService.createUser(userDTO, registerRequest.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}