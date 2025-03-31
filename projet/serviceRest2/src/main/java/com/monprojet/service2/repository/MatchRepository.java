package com.monprojet.service2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.monprojet.service2.model.Match;
import com.monprojet.service2.model.Team;
import com.monprojet.service2.model.Tournament;

import java.util.List;

@Repository
public interface MatchRepository extends CrudRepository<Match, Integer> {
    // Trouver les matchs par tournoi
    List<Match> findByTournament(Tournament tournament);
    
    // Trouver les matchs par équipe
    List<Match> findByTeam1OrTeam2(Team team1, Team team2);
} 