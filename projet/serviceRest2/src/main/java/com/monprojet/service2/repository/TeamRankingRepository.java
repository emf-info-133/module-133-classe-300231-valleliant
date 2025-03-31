package com.monprojet.service2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.monprojet.service2.model.Ranking;
import com.monprojet.service2.model.Team;
import com.monprojet.service2.model.TeamRanking;

import java.util.List;

@Repository
public interface TeamRankingRepository extends CrudRepository<TeamRanking, Integer> {
    // Trouver les classements par équipe
    List<TeamRanking> findByTeam(Team team);
    
    // Trouver les classements par ranking
    List<TeamRanking> findByRanking(Ranking ranking);
    
    // Trouver le classement d'une équipe dans un classement spécifique
    TeamRanking findByTeamAndRanking(Team team, Ranking ranking);
} 