package com.monprojet.service1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monprojet.service1.models.Team;
import com.monprojet.service1.models.Tournament;
import com.monprojet.service1.models.User;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findByTournament(Tournament tournament);
    List<Team> findByCaptain(User captain);
} 