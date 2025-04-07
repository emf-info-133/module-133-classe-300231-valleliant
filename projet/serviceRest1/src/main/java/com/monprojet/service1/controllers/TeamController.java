package com.monprojet.service1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monprojet.service1.dto.TeamDTO;
import com.monprojet.service1.services.TeamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "Équipes", description = "API pour gérer les équipes")
public class TeamController {
    
    private final TeamService teamService;
    
    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }
    
    @GetMapping
    @Operation(summary = "Récupérer toutes les équipes", description = "Renvoie la liste de toutes les équipes")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = teamService.getAllTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une équipe par ID", description = "Renvoie une équipe spécifique par son ID")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Integer id) {
        TeamDTO team = teamService.getTeamById(id);
        if (team != null) {
            return new ResponseEntity<>(team, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/tournament/{tournamentId}")
    @Operation(summary = "Récupérer les équipes par tournoi", description = "Renvoie la liste des équipes participant à un tournoi spécifique")
    public ResponseEntity<List<TeamDTO>> getTeamsByTournament(@PathVariable Integer tournamentId) {
        List<TeamDTO> teams = teamService.getTeamsByTournament(tournamentId);
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }
    
    @GetMapping("/captain/{captainId}")
    @Operation(summary = "Récupérer les équipes par capitaine", description = "Renvoie la liste des équipes dont l'utilisateur spécifié est capitaine")
    public ResponseEntity<List<TeamDTO>> getTeamsByCaptain(@PathVariable Integer captainId) {
        List<TeamDTO> teams = teamService.getTeamsByCaptain(captainId);
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }
    
    @PostMapping
    @Operation(summary = "Créer une nouvelle équipe", description = "Crée une nouvelle équipe et renvoie les détails")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamRequest teamRequest) {
        TeamDTO team = teamService.createTeam(
            teamRequest.getName(),
            teamRequest.getCaptainId(),
            teamRequest.getTournamentId()
        );
        if (team != null) {
            return new ResponseEntity<>(team, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une équipe", description = "Met à jour une équipe existante par son ID")
    public ResponseEntity<TeamDTO> updateTeam(
            @PathVariable Integer id,
            @RequestBody TeamRequest teamRequest) {
        TeamDTO team = teamService.updateTeam(
            id,
            teamRequest.getName(),
            teamRequest.getCaptainId(),
            teamRequest.getTournamentId()
        );
        if (team != null) {
            return new ResponseEntity<>(team, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une équipe", description = "Supprime une équipe par son ID")
    public ResponseEntity<Void> deleteTeam(@PathVariable Integer id) {
        boolean deleted = teamService.deleteTeam(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Classe de requête pour les équipes
    public static class TeamRequest {
        private String name;
        private Integer captainId;
        private Integer tournamentId;
        
        // Getters et Setters
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public Integer getCaptainId() {
            return captainId;
        }
        
        public void setCaptainId(Integer captainId) {
            this.captainId = captainId;
        }
        
        public Integer getTournamentId() {
            return tournamentId;
        }
        
        public void setTournamentId(Integer tournamentId) {
            this.tournamentId = tournamentId;
        }
    }
} 