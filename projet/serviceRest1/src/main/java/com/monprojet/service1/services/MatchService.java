package com.monprojet.service1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monprojet.service1.dto.MatchDTO;
import com.monprojet.service1.dto.TeamDTO;
import com.monprojet.service1.dto.TournamentDTO;
import com.monprojet.service1.dto.UserDTO;
import com.monprojet.service1.models.Match;
import com.monprojet.service1.models.Team;
import com.monprojet.service1.models.Tournament;
import com.monprojet.service1.repositories.MatchRepository;
import com.monprojet.service1.repositories.TeamRepository;
import com.monprojet.service1.repositories.TournamentRepository;

@Service
public class MatchService {
    
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    
    @Autowired
    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository, TournamentRepository tournamentRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
    }
    
    public List<MatchDTO> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public MatchDTO getMatchById(Integer id) {
        Optional<Match> match = matchRepository.findById(id);
        return match.map(this::convertToDTO).orElse(null);
    }
    
    public List<MatchDTO> getMatchesByTournament(Integer tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (!tournament.isPresent()) {
            return List.of();
        }
        
        return matchRepository.findByTournament(tournament.get()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<MatchDTO> getMatchesByTeam(Integer teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if (!team.isPresent()) {
            return List.of();
        }
        
        return matchRepository.findByTeam1OrTeam2(team.get(), team.get()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public MatchDTO createMatch(Integer tournamentId, Integer team1Id, Integer team2Id) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        Optional<Team> team1 = teamRepository.findById(team1Id);
        Optional<Team> team2 = teamRepository.findById(team2Id);
        
        if (!tournament.isPresent() || !team1.isPresent() || !team2.isPresent()) {
            return null;
        }
        
        Match match = new Match();
        match.setTournament(tournament.get());
        match.setTeam1(team1.get());
        match.setTeam2(team2.get());
        
        Match savedMatch = matchRepository.save(match);
        return convertToDTO(savedMatch);
    }
    
    public boolean deleteMatch(Integer id) {
        if (matchRepository.existsById(id)) {
            matchRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private MatchDTO convertToDTO(Match match) {
        TournamentDTO tournamentDTO = new TournamentDTO(
            match.getTournament().getId()
        );
        
        UserDTO captain1DTO = new UserDTO(
            match.getTeam1().getCaptain().getId(),
            match.getTeam1().getCaptain().getName(),
            match.getTeam1().getCaptain().getEmail()
        );
        
        UserDTO captain2DTO = new UserDTO(
            match.getTeam2().getCaptain().getId(),
            match.getTeam2().getCaptain().getName(),
            match.getTeam2().getCaptain().getEmail()
        );
        
        TeamDTO team1DTO = new TeamDTO(
            match.getTeam1().getId(),
            match.getTeam1().getName(),
            captain1DTO,
            tournamentDTO
        );
        
        TeamDTO team2DTO = new TeamDTO(
            match.getTeam2().getId(),
            match.getTeam2().getName(),
            captain2DTO,
            tournamentDTO
        );
        
        return new MatchDTO(
            match.getId(),
            tournamentDTO,
            team1DTO,
            team2DTO
        );
    }
} 