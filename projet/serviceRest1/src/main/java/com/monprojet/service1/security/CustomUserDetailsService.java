package com.monprojet.service1.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.monprojet.service1.models.User;
import com.monprojet.service1.repositories.UserRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Recherche de l'utilisateur par email
        Optional<User> userOptional = userRepository.findByEmail(identifier);

        // Si l'utilisateur n'est pas trouvé par email, on cherche par nom
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByName(identifier);
        }

        // Si aucun utilisateur n'est trouvé
        User user = userOptional.orElseThrow(
                () -> new UsernameNotFoundException("Utilisateur non trouvé avec l'identifiant : " + identifier));

        // Retourne un UserDetails avec l'email comme identifiant et le mot de passe
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())  // Le JWT contiendra l'email
                .password(user.getPassword())
                .authorities("USER")  // Rôle par défaut (tu peux ajuster selon les besoins)
                .build();
    }
}
