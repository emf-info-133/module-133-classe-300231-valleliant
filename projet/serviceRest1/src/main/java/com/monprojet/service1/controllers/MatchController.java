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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monprojet.service1.dto.MatchDTO;
import com.monprojet.service1.services.MatchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "Matches", description = "API pour gérer les matches")
public class MatchController {
    
    private final MatchService matchService;
    
    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }
    
    @GetMapping
    @Operation(summary = "Récupérer tous les matches", description = "Renvoie la liste de tous les matches")
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
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/tournament/{tournamentId}")
    @Operation(summary = "Récupérer les matches par tournoi", description = "Renvoie la liste des matches d'un tournoi spécifique")
    public ResponseEntity<List<MatchDTO>> getMatchesByTournament(@PathVariable Integer tournamentId) {
        List<MatchDTO> matches = matchService.getMatchesByTournament(tournamentId);
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }
    
    @GetMapping("/team/{teamId}")
    @Operation(summary = "Récupérer les matches par équipe", description = "Renvoie la liste des matches auxquels une équipe spécifique participe")
    public ResponseEntity<List<MatchDTO>> getMatchesByTeam(@PathVariable Integer teamId) {
        List<MatchDTO> matches = matchService.getMatchesByTeam(teamId);
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }
    
    @PostMapping
    @Operation(summary = "Créer un nouveau match", description = "Crée un nouveau match et renvoie les détails")
    public ResponseEntity<MatchDTO> createMatch(
            @RequestParam Integer tournamentId,
            @RequestParam Integer team1Id,
            @RequestParam Integer team2Id) {
        MatchDTO match = matchService.createMatch(tournamentId, team1Id, team2Id);
        if (match != null) {
            return new ResponseEntity<>(match, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un match", description = "Supprime un match par son ID")
    public ResponseEntity<Void> deleteMatch(@PathVariable Integer id) {
        boolean deleted = matchService.deleteMatch(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
} 