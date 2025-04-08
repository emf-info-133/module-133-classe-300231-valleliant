package com.monprojet.service1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.monprojet.service1.dto.UserDTO;
import com.monprojet.service1.models.User;
import com.monprojet.service1.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    
    

    // Créer un utilisateur
    public UserDTO createUser(UserDTO userDTO, String password) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(password); // Vous pouvez appliquer un encodeur de mot de passe ici
        user.setIsAdmin(0); // Par défaut, l'utilisateur n'est pas admin

        User savedUser = userRepository.save(user);
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
