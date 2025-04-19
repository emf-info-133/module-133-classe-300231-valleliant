package com.monprojet.service1.config;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.monprojet.service1.models.User;
import com.monprojet.service1.repositories.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Optional<User> existingUser = userRepository.findByEmail("admin@email.com");
            if (existingUser.isEmpty()) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@email.com");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setAdmin(true);
                userRepository.save(admin);
                System.out.println("👤 Utilisateur admin créé");
            } else {
                System.out.println("✔️ Admin déjà présent, rien à faire");
            }
        };
    }
}
