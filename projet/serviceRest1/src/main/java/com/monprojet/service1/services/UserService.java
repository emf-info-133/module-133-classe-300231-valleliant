package com.monprojet.service1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Amélioré : Récupérer un utilisateur par nom et lever une exception s'il n'est
    // pas trouvé
    

    public UserDTO getUserByName(String name) {
        return userRepository.findByName(name)
                  .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                  .orElse(null);
    }
    
    

    // Créer un utilisateur avec mot de passe haché
    public UserDTO createUser(UserDTO userDTO, String rawPassword) {
        // Hachage du mot de passe
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Créer l'entité User
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(hashedPassword);  // Stocker le mot de passe haché

        // Sauvegarder l'utilisateur dans la base de données
        User savedUser = userRepository.save(user);

        // Retourner le DTO
        return new UserDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    // Mettre à jour un utilisateur existant
    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            return null; // Ou lancer une exception spécifique
        }
        User user = userOpt.get();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        // Ici, on pourrait aussi gérer la mise à jour du mot de passe dans un endpoint
        // dédié
        User updatedUser = userRepository.save(user);
        return new UserDTO(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail());
    }

    // Supprimer un utilisateur par ID
    public boolean deleteUser(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
