package com.monprojet.service1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monprojet.service1.dto.TournamentDTO;
import com.monprojet.service1.models.Tournament;
import com.monprojet.service1.repositories.TournamentRepository;

@Service
public class TournamentService {
    
    private final TournamentRepository tournamentRepository;
    
    @Autowired
    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }
    
    public List<TournamentDTO> getAllTournaments() {
        return tournamentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public TournamentDTO getTournamentById(Integer id) {
        Optional<Tournament> tournament = tournamentRepository.findById(id);
        return tournament.map(this::convertToDTO).orElse(null);
    }
    
    public TournamentDTO createTournament() {
        Tournament tournament = new Tournament();
        Tournament savedTournament = tournamentRepository.save(tournament);
        return convertToDTO(savedTournament);
    }
    
    public boolean deleteTournament(Integer id) {
        if (tournamentRepository.existsById(id)) {
            tournamentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private TournamentDTO convertToDTO(Tournament tournament) {
        return new TournamentDTO(tournament.getId());
    }
} 