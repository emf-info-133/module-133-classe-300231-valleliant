package com.monprojet.service2.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.monprojet.service2.dto.MatchDTO;
import com.monprojet.service2.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "Matches", description = "API pour gérer les matchs")
public class MatchController {

    private final MatchService matchService;
    
    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }
    
    @GetMapping
    @Operation(summary = "Récupérer tous les matchs", description = "Renvoie la liste de tous les matchs")
    public ResponseEntity<List<MatchDTO>> getAllMatches() {
        List<MatchDTO> matches = matchService.getAllMatches();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un match par ID", description = "Renvoie un match spécifique par son ID")
    public ResponseEntity<MatchDTO> getMatchById(@PathVariable Integer id) {
        MatchDTO match = matchService.getMatchById(id);
        if (match != null) {
            return new ResponseEntity<>(match, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/tournament/{tournamentId}")
    @Operation(summary = "Récupérer les matchs d'un tournoi", description = "Renvoie la liste des matchs associés à un tournoi")
    public ResponseEntity<List<MatchDTO>> getMatchesByTournament(@PathVariable Integer tournamentId) {
        List<MatchDTO> matches = matchService.getMatchesByTournament(tournamentId);
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }
    
    // Endpoint pour récupérer les matchs d'une équipe
    @GetMapping("/team/{teamId}")
    @Operation(summary = "Récupérer les matchs d'une équipe", description = "Renvoie la liste des matchs dans lesquels l'équipe participe")
    public ResponseEntity<List<MatchDTO>> getMatchesByTeam(@PathVariable Integer teamId) {
        List<MatchDTO> matches = matchService.getMatchesByTeam(teamId);
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }
    
    @PostMapping
    @Operation(summary = "Créer un nouveau match", description = "Crée un nouveau match et renvoie ses détails")
    public ResponseEntity<MatchDTO> createMatch(@RequestBody MatchDTO matchDTO) {
        MatchDTO createdMatch = matchService.createMatch(matchDTO);
        return new ResponseEntity<>(createdMatch, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un match", description = "Met à jour un match existant par son ID")
    public ResponseEntity<MatchDTO> updateMatch(@PathVariable Integer id, @RequestBody MatchDTO matchDTO) {
        MatchDTO updatedMatch = matchService.updateMatch(id, matchDTO);
        if (updatedMatch != null) {
            return new ResponseEntity<>(updatedMatch, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un match", description = "Supprime un match par son ID")
    public ResponseEntity<Void> deleteMatch(@PathVariable Integer id) {
        boolean deleted = matchService.deleteMatch(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                       : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
