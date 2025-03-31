package com.monprojet.service2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.monprojet.service2.model.Tournament;

@Repository
public interface TournamentRepository extends CrudRepository<Tournament, Integer> {
    // Méthodes personnalisées si nécessaire
} 