package com.monprojet.service2.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.monprojet.service2.dto.MatchDTO;
import com.monprojet.service2.model.Match;
import com.monprojet.service2.repository.MatchRepository;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    
    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }
    
    public List<MatchDTO> getAllMatches() {
        List<Match> matches = matchRepository.findAll();
        return matches.stream()
                      .map(this::convertToDTO)
                      .collect(Collectors.toList());
    }
    
    public List<MatchDTO> getMatchesByTournament(Integer tournamentId) {
        List<Match> matches = matchRepository.findByTournamentId(tournamentId);
        return matches.stream()
                      .map(this::convertToDTO)
                      .collect(Collectors.toList());
    }
    
    public MatchDTO getMatchById(Integer id) {
        Optional<Match> matchOpt = matchRepository.findById(id);
        return matchOpt.map(this::convertToDTO)
                       .orElse(null);
    }

    public List<MatchDTO> getMatchesByTeam(Integer teamId) {
        List<Match> matches = matchRepository.findByTeam1IdOrTeam2Id(teamId, teamId);
        return matches.stream()
                      .map(this::convertToDTO)
                      .collect(Collectors.toList());
    }
    
    public MatchDTO createMatch(MatchDTO matchDTO) {
        Match match = new Match();
        match.setTournamentId(matchDTO.getTournamentId());
        match.setTeam1Id(matchDTO.getTeam1Id());
        match.setTeam2Id(matchDTO.getTeam2Id());
        match.setScore1(matchDTO.getScore1());
        match.setScore2(matchDTO.getScore2());
        match.setDate(java.time.LocalDate.parse(matchDTO.getDate()));
        
        Match saved = matchRepository.save(match);
        return convertToDTO(saved);
    }
    
    public MatchDTO updateMatch(Integer id, MatchDTO matchDTO) {
        Optional<Match> matchOpt = matchRepository.findById(id);
        if (!matchOpt.isPresent()) return null;
        Match match = matchOpt.get();
        match.setTeam1Id(matchDTO.getTeam1Id());
        match.setTeam2Id(matchDTO.getTeam2Id());
        match.setScore1(matchDTO.getScore1());
        match.setScore2(matchDTO.getScore2());
        match.setDate(java.time.LocalDate.parse(matchDTO.getDate()));
        Match updated = matchRepository.save(match);
        return convertToDTO(updated);
    }
    
    public boolean deleteMatch(Integer id) {
        if (matchRepository.existsById(id)) {
            matchRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private MatchDTO convertToDTO(Match match) {
        return new MatchDTO(
            match.getId(),
            match.getTournamentId(),
            match.getTeam1Id(),
            match.getTeam2Id(),
            match.getScore1(),
            match.getScore2(),
            match.getDate().toString()
        );
    }
}
