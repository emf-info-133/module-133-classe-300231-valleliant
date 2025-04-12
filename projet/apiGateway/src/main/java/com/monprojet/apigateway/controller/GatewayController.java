package com.monprojet.apigateway.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.monprojet.apigateway.dto.*;
import com.monprojet.apigateway.service.GatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

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
    public Mono<ResponseEntity<List<UserDTO>>> getUsers() {
        return gatewayService.getAllUsers()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Obtenir un utilisateur par ID")
    public Mono<ResponseEntity<UserDTO>> getUserById(@PathVariable Integer id) {
        return gatewayService.getUserById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/teams")
    @Operation(summary = "Obtenir toutes les équipes")
    public Mono<ResponseEntity<List<TeamDTO>>> getTeams() {
        return gatewayService.getAllTeams()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("/teams/{id}")
    @Operation(summary = "Obtenir une équipe par ID")
    public Mono<ResponseEntity<TeamDTO>> getTeamById(@PathVariable Integer id) {
        return gatewayService.getTeamById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // --- Endpoints pour ServiceRest2 (Tournois, Jeux, Matches) ---

    @GetMapping("/tournaments")
    @Operation(summary = "Obtenir tous les tournois")
    public Mono<ResponseEntity<List<TournamentDTO>>> getTournaments() {
        return gatewayService.getAllTournaments()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("/tournaments/{id}")
    @Operation(summary = "Obtenir un tournoi par ID")
    public Mono<ResponseEntity<TournamentDTO>> getTournamentById(@PathVariable Integer id) {
        return gatewayService.getTournamentById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/tournaments/{id}/with-admin")
    @Operation(summary = "Obtenir un tournoi avec administrateur", 
               description = "Rassemble les données du tournoi (serviceRest2) et de son admin (serviceRest1)")
    public Mono<ResponseEntity<TournamentWithAdminDTO>> getTournamentWithAdmin(@PathVariable Integer id) {
        return gatewayService.getTournamentWithAdmin(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/games")
    @Operation(summary = "Obtenir tous les jeux")
    public Mono<ResponseEntity<List<GameDTO>>> getGames() {
        return gatewayService.getAllGames()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("/matches")
    @Operation(summary = "Obtenir tous les matchs")
    public Mono<ResponseEntity<List<MatchDTO>>> getMatches() {
        return gatewayService.getAllMatches()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
    
    // Endpoint pour récupérer les matchs d'une équipe (par exemple)
    @GetMapping("/matches/team/{teamId}")
    @Operation(summary = "Obtenir les matchs d'une équipe")
    public Mono<ResponseEntity<List<MatchDTO>>> getMatchesByTeam(@PathVariable Integer teamId) {
        return gatewayService.getMatchesByTeam(teamId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
