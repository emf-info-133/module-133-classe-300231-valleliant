package com.monprojet.service1.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.monprojet.service1.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Utiliser Optional pour mieux gérer l'absence d'utilisateur
    Optional<User> findByName(String name);
}
