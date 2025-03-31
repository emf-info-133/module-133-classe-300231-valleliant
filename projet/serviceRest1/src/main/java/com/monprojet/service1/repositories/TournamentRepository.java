package com.monprojet.service1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monprojet.service1.models.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    // Méthodes personnalisées si nécessaire
} 