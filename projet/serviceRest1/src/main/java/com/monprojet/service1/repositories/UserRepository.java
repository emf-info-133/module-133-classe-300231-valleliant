package com.monprojet.service1.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monprojet.service1.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Recherche par email
    Optional<User> findByEmail(String email);

    // Recherche par nom
    Optional<User> findByName(String name);

    // Recherche par email ou nom
    Optional<User> findByEmailOrName(String email, String name);

}
