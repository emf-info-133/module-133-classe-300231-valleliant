package com.monprojet.service1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.monprojet.service1.dto.UserDTO;
import com.monprojet.service1.models.User;
import com.monprojet.service1.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Récupérer tous les utilisateurs
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
    }

    // Récupérer un utilisateur par ID
    public UserDTO getUserById(Integer id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .orElse(null);
    }

    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .orElse(null);
    }

    // Récupérer un utilisateur par nom
    public UserDTO getUserByName(String name) {
        return userRepository.findByName(name)
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .orElse(null);
    }

    public UserDTO createUser(UserDTO userDTO, String rawPassword) {
        // Vérification si les champs sont bien renseignés
        if (userDTO.getName() == null || userDTO.getEmail() == null || rawPassword == null) {
            throw new IllegalArgumentException("Le nom, l'email et le mot de passe sont obligatoires.");
        }

        // Vérifier si l'email existe déjà dans la base de données
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà.");
        }

        // Vérifier si le name existe déjà dans la base de données
        if (userRepository.findByName(userDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec ce nom existe déjà.");
        }

        // Hachage du mot de passe
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Créer l'entité User
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(hashedPassword); // Stocker le mot de passe haché
        user.setAdmin(false); // Valeur par défaut pour isAdmin (non administrateur)

        try {
            // Sauvegarder l'utilisateur dans la base de données
            User savedUser = userRepository.save(user);

            // Retourner le DTO avec les informations de l'utilisateur créé
            return new UserDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Erreur lors de la création de l'utilisateur : " + e.getMessage());
        }
    }

    // Mettre à jour un utilisateur existant
    public UserDTO updateUser(Integer id, String name, String email) {
        // Vérification si l'utilisateur existe dans la base de données
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("Utilisateur non trouvé");
        }
        User user = userOpt.get();

        // ⚠️ Vérification de doublon de nom (sauf soi-même)
        if (userRepository.existsByNameAndIdNot(name, id)) {
            throw new IllegalArgumentException("Un utilisateur avec ce nom existe déjà.");
        }

        // ⚠️ Vérification de doublon d'email (sauf soi-même)
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà.");
        }

        // Mise à jour des champs (nom et email)
        user.setName(name);
        user.setEmail(email);

        // Sauvegarder les modifications
        User updatedUser = userRepository.save(user);

        // Retourner le DTO avec les informations de l'utilisateur mis à jour
        return new UserDTO(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail());
    }

}
