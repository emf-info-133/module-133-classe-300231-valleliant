package com.monprojet.service2.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.monprojet.service2.dto.TournamentDTO;
import com.monprojet.service2.service.TournamentService;
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
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // Endpoint pour rechercher un tournoi par nom
    @GetMapping("/name/{name}")
    @Operation(summary = "Trouver un tournoi par nom", description = "Renvoie un tournoi correspondant au nom fourni")
    public ResponseEntity<TournamentDTO> getTournamentByName(@PathVariable String name) {
        TournamentDTO tournament = tournamentService.getTournamentByName(name);
        if (tournament != null) {
            return new ResponseEntity<>(tournament, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // Endpoint pour récupérer les tournois d'un jeu
    @GetMapping("/game/{gameId}")
    @Operation(summary = "Récupérer les tournois d'un jeu", description = "Renvoie la liste des tournois associés au jeu spécifié")
    public ResponseEntity<List<TournamentDTO>> getTournamentsByGameId(@PathVariable Integer gameId) {
        List<TournamentDTO> tournaments = tournamentService.getTournamentsByGameId(gameId);
        return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }
    
    @PostMapping
    @Operation(summary = "Créer un nouveau tournoi", description = "Crée un nouveau tournoi et renvoie ses détails")
    public ResponseEntity<TournamentDTO> createTournament(@RequestBody TournamentDTO tournamentDTO) {
        TournamentDTO createdTournament = tournamentService.createTournament(tournamentDTO);
        return new ResponseEntity<>(createdTournament, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un tournoi", description = "Met à jour un tournoi existant par son ID")
    public ResponseEntity<TournamentDTO> updateTournament(
            @PathVariable Integer id,
            @RequestBody TournamentDTO tournamentDTO) {
        TournamentDTO updatedTournament = tournamentService.updateTournament(id, tournamentDTO);
        if (updatedTournament != null) {
            return new ResponseEntity<>(updatedTournament, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un tournoi", description = "Supprime un tournoi par son ID")
    public ResponseEntity<Void> deleteTournament(@PathVariable Integer id) {
        boolean deleted = tournamentService.deleteTournament(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                       : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
