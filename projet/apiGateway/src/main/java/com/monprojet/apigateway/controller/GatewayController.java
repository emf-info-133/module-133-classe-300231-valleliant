package com.monprojet.apigateway.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.monprojet.apigateway.dto.*;
import com.monprojet.apigateway.service.GatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "API Gateway", description = "Points d'entrée centralisés vers ServiceRest1 et ServiceRest2")
public class GatewayController {

    private final GatewayService gatewayService;

    @Autowired
    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    // --- Endpoints pour ServiceRest1 (Utilisateurs et Équipes) ---
    
    @GetMapping("/users")
    @Operation(summary = "Obtenir tous les utilisateurs")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(gatewayService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Obtenir un utilisateur par ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO user = gatewayService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/teams")
    @Operation(summary = "Obtenir toutes les équipes")
    public ResponseEntity<List<TeamDTO>> getTeams() {
        return ResponseEntity.ok(gatewayService.getAllTeams());
    }

    @GetMapping("/teams/{id}")
    @Operation(summary = "Obtenir une équipe par ID")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Integer id) {
        TeamDTO team = gatewayService.getTeamById(id);
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }

    // --- Endpoints pour ServiceRest2 (Tournois, Jeux, Matches) ---

    @GetMapping("/tournaments")
    @Operation(summary = "Obtenir tous les tournois")
    public ResponseEntity<List<TournamentDTO>> getTournaments() {
        return ResponseEntity.ok(gatewayService.getAllTournaments());
    }

    @GetMapping("/tournaments/{id}")
    @Operation(summary = "Obtenir un tournoi par ID")
    public ResponseEntity<TournamentDTO> getTournamentById(@PathVariable Integer id) {
        TournamentDTO tournament = gatewayService.getTournamentById(id);
        return tournament != null ? ResponseEntity.ok(tournament) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/tournaments/{id}/with-admin")
    @Operation(summary = "Obtenir un tournoi avec administrateur", 
               description = "Rassemble les données du tournoi (serviceRest2) et de son admin (serviceRest1)")
    public ResponseEntity<TournamentWithAdminDTO> getTournamentWithAdmin(@PathVariable Integer id) {
        TournamentWithAdminDTO dto = gatewayService.getTournamentWithAdmin(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/games")
    @Operation(summary = "Obtenir tous les jeux")
    public ResponseEntity<List<GameDTO>> getGames() {
        return ResponseEntity.ok(gatewayService.getAllGames());
    }

    @GetMapping("/matches")
    @Operation(summary = "Obtenir tous les matchs")
    public ResponseEntity<List<MatchDTO>> getMatches() {
        return ResponseEntity.ok(gatewayService.getAllMatches());
    }
    
    // Endpoint pour récupérer les matchs d'une équipe (par exemple)
    @GetMapping("/matches/team/{teamId}")
    @Operation(summary = "Obtenir les matchs d'une équipe")
    public ResponseEntity<List<MatchDTO>> getMatchesByTeam(@PathVariable Integer teamId) {
        return ResponseEntity.ok(gatewayService.getMatchesByTeam(teamId));
    }
}
