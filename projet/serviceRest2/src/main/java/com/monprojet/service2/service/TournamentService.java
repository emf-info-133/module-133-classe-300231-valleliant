package com.monprojet.service2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monprojet.service2.dto.GameDTO;
import com.monprojet.service2.dto.TournamentDTO;
import com.monprojet.service2.model.Game;
import com.monprojet.service2.model.Tournament;
import com.monprojet.service2.repository.GameRepository;
import com.monprojet.service2.repository.TournamentRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {
    
    private final TournamentRepository tournamentRepository;
    private final GameRepository gameRepository;
    
    @Autowired
    public TournamentService(TournamentRepository tournamentRepository, 
                             GameRepository gameRepository) {
        this.tournamentRepository = tournamentRepository;
        this.gameRepository = gameRepository;
    }
    
    // Récupérer tous les tournois
    public List<TournamentDTO> getAllTournaments() {
        List<TournamentDTO> tournamentDTOs = new ArrayList<>();
        tournamentRepository.findAll().forEach(tournament -> {
            tournamentDTOs.add(convertToDTO(tournament));
        });
        return tournamentDTOs;
    }
    
    // Récupérer un tournoi par ID
    public TournamentDTO getTournamentById(Integer id) {
        Optional<Tournament> tournament = tournamentRepository.findById(id);
        return tournament.map(this::convertToDTO).orElse(null);
    }
    
    // Ajouter un nouveau tournoi
    @Transactional
    public TournamentDTO addTournament(String name, String date, Integer gameId, Integer adminId, Boolean status) {
        Optional<Game> gameOpt = gameRepository.findById(gameId);
        
        if (!gameOpt.isPresent()) {
            return null;
        }
        
        Tournament tournament = new Tournament();
        tournament.setName(name);
        tournament.setDate(date);
        tournament.setGame(gameOpt.get());
        tournament.setAdminId(adminId);
        tournament.setStatus(status);
        
        Tournament savedTournament = tournamentRepository.save(tournament);
        return convertToDTO(savedTournament);
    }
    
    // Mettre à jour un tournoi existant
    @Transactional
    public TournamentDTO updateTournament(Integer id, String name, String date, 
                                        Integer gameId, Integer adminId, Boolean status) {
        Optional<Tournament> tournamentOpt = tournamentRepository.findById(id);
        
        if (!tournamentOpt.isPresent()) {
            return null;
        }
        
        Tournament tournament = tournamentOpt.get();
        
        if (name != null) tournament.setName(name);
        if (date != null) tournament.setDate(date);
        
        if (gameId != null) {
            Optional<Game> gameOpt = gameRepository.findById(gameId);
            if (gameOpt.isPresent()) {
                tournament.setGame(gameOpt.get());
            }
        }
        
        if (adminId != null) {
            tournament.setAdminId(adminId);
        }
        
        if (status != null) {
            tournament.setStatus(status);
        }
        
        Tournament updatedTournament = tournamentRepository.save(tournament);
        return convertToDTO(updatedTournament);
    }
    
    // Supprimer un tournoi
    @Transactional
    public boolean deleteTournament(Integer id) {
        if (tournamentRepository.existsById(id)) {
            tournamentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Convertir un Tournament en TournamentDTO
    private TournamentDTO convertToDTO(Tournament tournament) {
        GameDTO gameDTO = new GameDTO(
            tournament.getGame().getId(),
            tournament.getGame().getName(),
            tournament.getGame().getType()
        );
        
        return new TournamentDTO(
            tournament.getId(),
            tournament.getName(),
            tournament.getDate(),
            gameDTO,
            tournament.getAdminId(),
            tournament.getStatus()
        );
    }
} 