package com.monprojet.service1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.monprojet.service1.models.User;
import com.monprojet.service1.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    // D'abord essayer par email
    Optional<User> optionalUser = userRepository.findByEmail(identifier);

    // Si pas trouvé, essayer par nom
    if (optionalUser.isEmpty()) {
        optionalUser = userRepository.findByName(identifier);
    }

    // Si toujours pas trouvé, lever une exception
    User user = optionalUser.orElseThrow(() ->
        new UsernameNotFoundException("Utilisateur non trouvé avec l'identifiant : " + identifier)
    );

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), // identifiant utilisé en interne par Spring
        user.getPassword(),
        Collections.singleton(new SimpleGrantedAuthority(user.isAdmin() ? "ROLE_ADMIN" : "ROLE_USER"))
    );
}


}
