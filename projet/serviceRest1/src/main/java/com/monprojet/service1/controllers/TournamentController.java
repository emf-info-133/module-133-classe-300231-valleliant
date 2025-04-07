package com.monprojet.service1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monprojet.service1.dto.TournamentDTO;
import com.monprojet.service1.services.TournamentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tournaments")
@Tag(name = "Tournois", description = "API pour gérer les tournois")
public class TournamentController {
    
    private final TournamentService tournamentService;
    
    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }
    
    @GetMapping
    @Operation(summary = "Récupérer tous les tournois", description = "Renvoie la liste de tous les tournois")
    public ResponseEntity<List<TournamentDTO>> getAllTournaments() {
        List<TournamentDTO> tournaments = tournamentService.getAllTournaments();
        return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un tournoi par ID", description = "Renvoie un tournoi spécifique par son ID")
    public ResponseEntity<TournamentDTO> getTournamentById(@PathVariable Integer id) {
        TournamentDTO tournament = tournamentService.getTournamentById(id);
        if (tournament != null) {
            return new ResponseEntity<>(tournament, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping
    @Operation(summary = "Créer un nouveau tournoi", description = "Crée un nouveau tournoi et renvoie les détails")
    public ResponseEntity<TournamentDTO> createTournament() {
        TournamentDTO tournament = tournamentService.createTournament();
        return new ResponseEntity<>(tournament, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un tournoi", description = "Supprime un tournoi par son ID")
    public ResponseEntity<Void> deleteTournament(@PathVariable Integer id) {
        boolean deleted = tournamentService.deleteTournament(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
} 