package com.monprojet.service2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.monprojet.service2.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    // Méthodes personnalisées si nécessaire
}
