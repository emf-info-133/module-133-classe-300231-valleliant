package com.monprojet.apigateway.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.monprojet.apigateway.dto.UserDTO;
import com.monprojet.apigateway.service.GatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
 
@RestController
@RequestMapping("/register")
@Tag(name = "Inscription", description = "Endpoint pour créer un compte utilisateur")
public class RegisterController {
 
    private final GatewayService userService;
    @Autowired
    public RegisterController(GatewayService userService) {
        this.userService = userService;
    }
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        // Getters / Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    @PostMapping
    @Operation(summary = "Créer un compte utilisateur", description = "Inscrit un nouvel utilisateur avec hash du mot de passe")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest registerRequest) {
        UserDTO userDTO = new UserDTO(null, registerRequest.getName(), registerRequest.getEmail());
        UserDTO createdUser = userService.createUser(userDTO, registerRequest.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}