package com.monprojet.apigateway.service;
 
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.monprojet.apigateway.model.User;
import com.monprojet.apigateway.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
 
@Service
public class CustomUserDetailsService implements UserDetailsService {
 
    private final UserRepository userRepository;
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Recherche par email
        Optional<User> optionalUser = userRepository.findByEmail(identifier);
        // Sinon par nom
        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByName(identifier);
        }
        User user = optionalUser.orElseThrow(() ->
            new UsernameNotFoundException("Utilisateur non trouvé avec l'identifiant : " + identifier)
        );
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(), // Utilisation de l'email comme identifiant interne
            user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority(user.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER"))
        );
    }
}