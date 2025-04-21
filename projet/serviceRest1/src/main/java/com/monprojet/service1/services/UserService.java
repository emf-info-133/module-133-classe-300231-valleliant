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

/**
 * Service qui gère les utilisateurs du système.
 * Contient des méthodes pour récupérer, créer, et mettre à jour des utilisateurs.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructeur pour initialiser le service avec le dépôt des utilisateurs et l'encodeur de mot de passe.
     * 
     * @param userRepository Le repository des utilisateurs.
     * @param passwordEncoder L'encodeur de mot de passe.
     */
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Récupère tous les utilisateurs du système sous forme de DTO.
     * 
     * @return Une liste de {@link UserDTO} représentant tous les utilisateurs.
     */
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.isAdmin()))
                .collect(Collectors.toList());
    }

    /**
     * Récupère un utilisateur en fonction de son identifiant.
     * 
     * @param id L'identifiant de l'utilisateur à récupérer.
     * @return Un objet {@link UserDTO} représentant l'utilisateur trouvé, ou null si l'utilisateur n'existe pas.
     */
    public UserDTO getUserById(Integer id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.isAdmin()))
                .orElse(null);
    }

    /**
     * Récupère un utilisateur en fonction de son adresse e-mail.
     * 
     * @param email L'e-mail de l'utilisateur à récupérer.
     * @return Un objet {@link UserDTO} représentant l'utilisateur trouvé, ou null si l'utilisateur n'existe pas.
     */
    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.isAdmin()))
                .orElse(null);
    }

    /**
     * Récupère un utilisateur en fonction de son nom.
     * 
     * @param name Le nom de l'utilisateur à récupérer.
     * @return Un objet {@link UserDTO} représentant l'utilisateur trouvé, ou null si l'utilisateur n'existe pas.
     */
    public UserDTO getUserByName(String name) {
        return userRepository.findByName(name)
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.isAdmin()))
                .orElse(null);
    }

    /**
     * Crée un nouvel utilisateur avec un mot de passe brut, et le retourne en tant que DTO.
     * 
     * @param userDTO L'objet DTO représentant les informations de l'utilisateur.
     * @param rawPassword Le mot de passe brut de l'utilisateur.
     * @return Un objet {@link UserDTO} représentant l'utilisateur créé.
     * @throws IllegalArgumentException Si l'email ou le nom de l'utilisateur existe déjà.
     */
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
            return new UserDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.isAdmin());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Erreur lors de la création de l'utilisateur : " + e.getMessage());
        }
    }

    /**
     * Met à jour les informations d'un utilisateur existant (nom et email).
     * 
     * @param id L'identifiant de l'utilisateur à mettre à jour.
     * @param name Le nouveau nom de l'utilisateur.
     * @param email Le nouvel e-mail de l'utilisateur.
     * @return Un objet {@link UserDTO} représentant l'utilisateur mis à jour.
     * @throws IllegalArgumentException Si l'utilisateur n'existe pas ou si le nom/email existe déjà.
     */
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
        return new UserDTO(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(), updatedUser.isAdmin());
    }
}
