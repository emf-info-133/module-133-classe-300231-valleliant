package com.monprojet.service1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monprojet.service1.dto.TeamDTO;
import com.monprojet.service1.dto.TournamentDTO;
import com.monprojet.service1.dto.UserDTO;
import com.monprojet.service1.models.Team;
import com.monprojet.service1.models.Tournament;
import com.monprojet.service1.models.User;
import com.monprojet.service1.repositories.TeamRepository;
import com.monprojet.service1.repositories.TournamentRepository;
import com.monprojet.service1.repositories.UserRepository;

@Service
public class TeamService {
    
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;
    
    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository, TournamentRepository tournamentRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
    }
    
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public TeamDTO getTeamById(Integer id) {
        Optional<Team> team = teamRepository.findById(id);
        return team.map(this::convertToDTO).orElse(null);
    }
    
    public List<TeamDTO> getTeamsByTournament(Integer tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (!tournament.isPresent()) {
            return List.of();
        }
        
        return teamRepository.findByTournament(tournament.get()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<TeamDTO> getTeamsByCaptain(Integer captainId) {
        Optional<User> captain = userRepository.findById(captainId);
        if (!captain.isPresent()) {
            return List.of();
        }
        
        return teamRepository.findByCaptain(captain.get()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public TeamDTO createTeam(String name, Integer captainId, Integer tournamentId) {
        Optional<User> captain = userRepository.findById(captainId);
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        
        if (!captain.isPresent() || !tournament.isPresent()) {
            return null;
        }
        
        Team team = new Team();
        team.setName(name);
        team.setCaptain(captain.get());
        team.setTournament(tournament.get());
        
        Team savedTeam = teamRepository.save(team);
        return convertToDTO(savedTeam);
    }
    
    public TeamDTO updateTeam(Integer id, String name, Integer captainId, Integer tournamentId) {
        Optional<Team> teamOpt = teamRepository.findById(id);
        if (!teamOpt.isPresent()) {
            return null;
        }
        
        Team team = teamOpt.get();
        
        if (name != null) {
            team.setName(name);
        }
        
        if (captainId != null) {
            Optional<User> captain = userRepository.findById(captainId);
            if (captain.isPresent()) {
                team.setCaptain(captain.get());
            }
        }
        
        if (tournamentId != null) {
            Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
            if (tournament.isPresent()) {
                team.setTournament(tournament.get());
            }
        }
        
        Team updatedTeam = teamRepository.save(team);
        return convertToDTO(updatedTeam);
    }
    
    public boolean deleteTeam(Integer id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private TeamDTO convertToDTO(Team team) {
        UserDTO captainDTO = new UserDTO(
            team.getCaptain().getId(),
            team.getCaptain().getName(),
            team.getCaptain().getEmail()
        );
        
        TournamentDTO tournamentDTO = new TournamentDTO(
            team.getTournament().getId()
        );
        
        return new TeamDTO(
            team.getId(),
            team.getName(),
            captainDTO,
            tournamentDTO
        );
    }
} 