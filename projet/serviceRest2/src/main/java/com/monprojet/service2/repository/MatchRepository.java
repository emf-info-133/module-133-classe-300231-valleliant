package com.monprojet.service2.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.monprojet.service2.model.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByTeam1IdOrTeam2Id(Integer team1Id, Integer team2Id);
    
    // Méthode existante pour filtrer par tournoi
    List<Match> findByTournamentId(Integer tournamentId);
}
