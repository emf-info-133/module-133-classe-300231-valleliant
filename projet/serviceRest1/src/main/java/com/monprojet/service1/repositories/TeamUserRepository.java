package com.monprojet.service1.repositories;

import com.monprojet.service1.models.TeamUser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamUserRepository extends JpaRepository<TeamUser, Integer> {

    Optional<TeamUser> findByUserIdAndTeamId(Integer userId, Integer teamId);

    boolean existsByUser_IdAndTeam_TournamentId(Integer userId, Integer tournamentId);
}
