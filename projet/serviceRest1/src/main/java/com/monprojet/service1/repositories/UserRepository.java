package com.monprojet.service1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monprojet.service1.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Méthodes personnalisées si nécessaire
} 