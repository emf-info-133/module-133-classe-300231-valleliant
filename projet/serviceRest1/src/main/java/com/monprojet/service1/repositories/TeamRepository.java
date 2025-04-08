package com.monprojet.service1.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.monprojet.service1.models.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findByCaptainId(Integer captainId);
    List<Team> findByTournamentId(Integer tournamentId);
}
