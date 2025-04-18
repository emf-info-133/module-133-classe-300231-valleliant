package com.monprojet.service1.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) {
        if (teamDTO.getName() == null || teamDTO.getCaptain() == null || teamDTO.getTournament() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        TeamDTO team = teamService.createTeam(
                teamDTO.getName(),
                teamDTO.getCaptain(),
                teamDTO.getTournament());

        return new ResponseEntity<>(team, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une équipe", description = "Met à jour une équipe existante par son ID")
    public ResponseEntity<TeamDTO> updateTeam(
            @PathVariable Integer id,
            @RequestBody TeamDTO teamDTO) {

        try {
            // Vérification des champs obligatoires
            if (teamDTO.getName() == null || teamDTO.getName().trim().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (teamDTO.getCaptain() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (teamDTO.getTournament() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Appel au service
            TeamDTO updatedTeam = teamService.updateTeam(
                    id,
                    teamDTO.getName(),
                    teamDTO.getCaptain(),
                    teamDTO.getTournament());

            return new ResponseEntity<>(updatedTeam, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace(); // utile pour le debug temporairement
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une équipe", description = "Supprime une équipe par son ID")
    public ResponseEntity<Void> deleteTeam(@PathVariable Integer id) {
        boolean deleted = teamService.deleteTeam(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
