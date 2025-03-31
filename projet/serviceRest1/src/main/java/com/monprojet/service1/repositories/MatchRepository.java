package com.monprojet.service1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monprojet.service1.models.Match;
import com.monprojet.service1.models.Team;
import com.monprojet.service1.models.Tournament;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByTournament(Tournament tournament);
    List<Match> findByTeam1OrTeam2(Team team1, Team team2);
} 