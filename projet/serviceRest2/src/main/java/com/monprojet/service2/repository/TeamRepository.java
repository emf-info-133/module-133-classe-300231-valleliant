package com.monprojet.service2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.monprojet.service2.model.Team;
import com.monprojet.service2.model.Tournament;

import java.util.List;

@Repository
public interface TeamRepository extends CrudRepository<Team, Integer> {
    // Trouver les équipes par tournoi
    List<Team> findByTournament(Tournament tournament);
} 