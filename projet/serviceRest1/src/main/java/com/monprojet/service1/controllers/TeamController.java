package com.monprojet.service1.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.monprojet.service1.dto.TeamDTO;
import com.monprojet.service1.services.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Contrôleur qui gère les opérations liées aux équipes.
 * Il permet aux utilisateurs de récupérer, créer, mettre à jour et supprimer
 * des équipes.
 */
@RestController
@RequestMapping("/api/teams")
@Tag(name = "Équipes", description = "API pour gérer les équipes")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Récupère toutes les équipes.
     * 
     * @return Une liste de DTO représentant toutes les équipes.
     */
    @GetMapping
    @Operation(summary = "Récupérer toutes les équipes", description = "Renvoie la liste de toutes les équipes")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = teamService.getAllTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    /**
     * Récupère une équipe par son ID.
     * 
     * @param id L'ID de l'équipe à récupérer.
     * @return Un DTO représentant l'équipe, ou une erreur 404 si l'équipe n'est pas
     *         trouvée.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une équipe par ID", description = "Renvoie une équipe spécifique par son ID")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Integer id) {
        TeamDTO team = teamService.getTeamById(id);
        if (team != null) {
            return new ResponseEntity<>(team, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Récupère toutes les équipes par tournoi.
     * 
     * @param tournamentId L'ID du tournoi pour lequel récupérer les équipes.
     * @return Une liste de DTO représentant les équipes de ce tournoi.
     */
    @GetMapping("/tournament/{tournamentId}")
    @Operation(summary = "Récupérer les équipes par tournoi", description = "Renvoie la liste des équipes participant à un tournoi spécifique")
    public ResponseEntity<List<TeamDTO>> getTeamsByTournament(@PathVariable Integer tournamentId) {
        List<TeamDTO> teams = teamService.getTeamsByTournament(tournamentId);
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    /**
     * Récupère toutes les équipes par capitaine.
     * 
     * @param captainId L'ID du capitaine pour lequel récupérer les équipes.
     * @return Une liste de DTO représentant les équipes dirigées par ce capitaine.
     */
    @GetMapping("/captain/{captainId}")
    @Operation(summary = "Récupérer les équipes par capitaine", description = "Renvoie la liste des équipes dont l'utilisateur spécifié est capitaine")
    public ResponseEntity<List<TeamDTO>> getTeamsByCaptain(@PathVariable Integer captainId) {
        List<TeamDTO> teams = teamService.getTeamsByCaptain(captainId);
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    /**
     * Crée une nouvelle équipe.
     * 
     * @param teamDTO Le DTO contenant les informations de l'équipe à créer.
     * @return Le DTO de l'équipe créée, ou une erreur 400 si des informations sont
     *         manquantes.
     */
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

    /**
     * Met à jour une équipe existante.
     * 
     * @param id      L'ID de l'équipe à mettre à jour.
     * @param teamDTO Le DTO contenant les nouvelles informations de l'équipe.
     * @return Le DTO de l'équipe mise à jour, ou une erreur 400 si des informations
     *         sont manquantes.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une équipe", description = "Met à jour une équipe existante par son ID")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable Integer id, @RequestBody TeamDTO teamDTO) {
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

    /**
     * Supprime une équipe par son ID.
     * 
     * @param id L'ID de l'équipe à supprimer.
     * @return Une réponse HTTP indiquant si l'équipe a été supprimée ou non.
     */
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
