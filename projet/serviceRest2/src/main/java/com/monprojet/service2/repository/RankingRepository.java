package com.monprojet.service2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.monprojet.service2.model.Ranking;
import com.monprojet.service2.model.Tournament;

import java.util.Optional;

@Repository
public interface RankingRepository extends CrudRepository<Ranking, Integer> {
    // Trouver le classement par tournoi
    Optional<Ranking> findByTournament(Tournament tournament);
} 