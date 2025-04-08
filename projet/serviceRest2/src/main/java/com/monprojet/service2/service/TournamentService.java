package com.monprojet.service2.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.monprojet.service2.dto.TournamentDTO;
import com.monprojet.service2.model.Tournament;
import com.monprojet.service2.repository.TournamentRepository;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    
    @Autowired
    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }
    
    public List<TournamentDTO> getAllTournaments() {
        List<Tournament> tournaments = tournamentRepository.findAll();
        return tournaments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    public TournamentDTO getTournamentById(Integer id) {
        Optional<Tournament> tournament = tournamentRepository.findById(id);
        return tournament.map(this::convertToDTO).orElse(null);
    }
    
    // Créer un tournoi
    public TournamentDTO createTournament(TournamentDTO tournamentDTO) {
        Tournament tournament = new Tournament();
        tournament.setName(tournamentDTO.getName());
        tournament.setDate(tournamentDTO.getDate());
        tournament.setAdminId(tournamentDTO.getAdminId());
        tournament.setGameId(tournamentDTO.getGameId());
        Tournament saved = tournamentRepository.save(tournament);
        return convertToDTO(saved);
    }
    
    // Mettre à jour un tournoi
    public TournamentDTO updateTournament(Integer id, TournamentDTO tournamentDTO) {
        Optional<Tournament> opt = tournamentRepository.findById(id);
        if (!opt.isPresent()) {
            return null;
        }
        Tournament tournament = opt.get();
        tournament.setName(tournamentDTO.getName());
        tournament.setDate(tournamentDTO.getDate());
        tournament.setAdminId(tournamentDTO.getAdminId());
        tournament.setGameId(tournamentDTO.getGameId());
        Tournament updated = tournamentRepository.save(tournament);
        return convertToDTO(updated);
    }
    
    public boolean deleteTournament(Integer id) {
        if (tournamentRepository.existsById(id)) {
            tournamentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Nouvelle méthode: Recherche d'un tournoi par son nom
    public TournamentDTO getTournamentByName(String name) {
        Optional<Tournament> tournament = tournamentRepository.findByName(name);
        return tournament.map(this::convertToDTO).orElse(null);
    }
    
    // Nouvelle méthode: Recherche des tournois par l'id d'un jeu
    public List<TournamentDTO> getTournamentsByGameId(Integer gameId) {
        List<Tournament> tournaments = tournamentRepository.findByGameId(gameId);
        return tournaments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    // Méthode utilitaire pour convertir l'entité Tournament en TournamentDTO
    private TournamentDTO convertToDTO(Tournament tournament) {
        return new TournamentDTO(
            tournament.getId(),
            tournament.getName(),
            tournament.getDate(),
            tournament.getAdminId(),
            tournament.getGameId()
        );
    }
}
