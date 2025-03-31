package com.monprojet.service2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.monprojet.service2.model.Game;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {
    // Méthodes personnalisées si nécessaire
} 