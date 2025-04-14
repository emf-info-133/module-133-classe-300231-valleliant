package com.monprojet.apigateway.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.monprojet.apigateway.dto.*;
import com.monprojet.apigateway.service.GatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "API Gateway", description = "Points d'entrée centralisés vers ServiceRest1 et ServiceRest2")
public class GatewayController {

    private final GatewayService gatewayService;

    @Autowired
    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    // Méthode pour vérifier si l'utilisateur est connecté
    private boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);  // Ne pas créer de nouvelle session
        return session != null && session.getAttribute("user") != null;
    }

    // Méthode pour vérifier si l'utilisateur est administrateur
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);  // Ne pas créer de nouvelle session
        if (session != null && session.getAttribute("user") != null) {
            String username = (String) session.getAttribute("user");
            // Vérifiez si l'utilisateur a l'email admin@admin.com
            return "admin@admin.com".equals(username);
        }
        return false;  // Par défaut, l'utilisateur n'est pas admin
    }

    // --- Endpoints pour ServiceRest1 (Utilisateurs et Équipes) ---

    @GetMapping("/users")
    @Operation(summary = "Obtenir tous les utilisateurs")
    public ResponseEntity<List<UserDTO>> getUsers(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).body(null);  // Non autorisé si l'utilisateur n'est pas connecté
        }

        List<UserDTO> users = gatewayService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Obtenir un utilisateur par ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).body(null);  // Non autorisé si l'utilisateur n'est pas connecté
        }

        UserDTO user = gatewayService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/teams")
    @Operation(summary = "Obtenir toutes les équipes")
    public ResponseEntity<List<TeamDTO>> getTeams(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).body(null);  // Non autorisé si l'utilisateur n'est pas connecté
        }

        List<TeamDTO> teams = gatewayService.getAllTeams();
        return teams.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(teams);
    }

    @GetMapping("/teams/{id}")
    @Operation(summary = "Obtenir une équipe par ID")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).body(null);  // Non autorisé si l'utilisateur n'est pas connecté
        }

        TeamDTO team = gatewayService.getTeamById(id);
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }

    // --- Endpoints réservés aux administrateurs (Matchs et Tournois) ---

    @GetMapping("/admin/tournaments")
    @Operation(summary = "Obtenir tous les tournois (réservé aux administrateurs)")
    public ResponseEntity<List<TournamentDTO>> getTournaments(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).body(null);  // Non autorisé si l'utilisateur n'est pas connecté
        }

        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body(null);  // Accès interdit si l'utilisateur n'est pas admin
        }

        List<TournamentDTO> tournaments = gatewayService.getAllTournaments();
        return tournaments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tournaments);
    }

    @PostMapping("/admin/tournaments")
    @Operation(summary = "Créer un tournoi (réservé aux administrateurs)")
    public ResponseEntity<String> createTournament(@RequestBody TournamentDTO tournamentDTO, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).body("Non autorisé : Veuillez vous connecter.");
        }

        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body("Accès interdit : Vous n'êtes pas administrateur.");
        }

        gatewayService.createTournament(tournamentDTO);
        return ResponseEntity.status(201).body("Tournoi créé avec succès.");
    }

    @PutMapping("/admin/tournaments/{id}")
    @Operation(summary = "Mettre à jour un tournoi (réservé aux administrateurs)")
    public ResponseEntity<String> updateTournament(@PathVariable Integer id, @RequestBody TournamentDTO tournamentDTO, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).body("Non autorisé : Veuillez vous connecter.");
        }

        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body("Accès interdit : Vous n'êtes pas administrateur.");
        }

        boolean updated = gatewayService.updateTournament(id, tournamentDTO);
        return updated ? ResponseEntity.ok("Tournoi mis à jour.") : ResponseEntity.status(404).body("Tournoi non trouvé.");
    }

    @DeleteMapping("/admin/tournaments/{id}")
    @Operation(summary = "Supprimer un tournoi (réservé aux administrateurs)")
    public ResponseEntity<String> deleteTournament(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).body("Non autorisé : Veuillez vous connecter.");
        }

        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body("Accès interdit : Vous n'êtes pas administrateur.");
        }

        boolean deleted = gatewayService.deleteTournament(id);
        return deleted ? ResponseEntity.ok("Tournoi supprimé.") : ResponseEntity.status(404).body("Tournoi non trouvé.");
    }

    @GetMapping("/admin/matches")
    @Operation(summary = "Obtenir tous les matchs (réservé aux administrateurs)")
    public ResponseEntity<List<MatchDTO>> getMatches(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).body(null);  // Non autorisé si l'utilisateur n'est pas connecté
        }

        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body(null);  // Accès interdit si l'utilisateur n'est pas admin
        }

        List<MatchDTO> matches = gatewayService.getAllMatches();
        return matches.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(matches);
    }

    @PostMapping("/admin/matches")
    @Operation(summary = "Créer un match (réservé aux administrateurs)")
    public ResponseEntity<String> createMatch(@RequestBody MatchDTO matchDTO, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).body("Non autorisé : Veuillez vous connecter.");
        }

        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body("Accès interdit : Vous n'êtes pas administrateur.");
        }

        gatewayService.createMatch(matchDTO);
        return ResponseEntity.status(201).body("Match créé avec succès.");
    }

    @PutMapping("/admin/matches/{id}")
    @Operation(summary = "Mettre à jour un match (réservé aux administrateurs)")
    public ResponseEntity<String> updateMatch(@PathVariable Integer id, @RequestBody MatchDTO matchDTO, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).body("Non autorisé : Veuillez vous connecter.");
        }

        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body("Accès interdit : Vous n'êtes pas administrateur.");
        }

        boolean updated = gatewayService.updateMatch(id, matchDTO);
        return updated ? ResponseEntity.ok("Match mis à jour.") : ResponseEntity.status(404).body("Match non trouvé.");
    }

    @DeleteMapping("/admin/matches/{id}")
    @Operation(summary = "Supprimer un match (réservé aux administrateurs)")
    public ResponseEntity<String> deleteMatch(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).body("Non autorisé : Veuillez vous connecter.");
        }

        if (!isAdmin(request)) {
            return ResponseEntity.status(403).body("Accès interdit : Vous n'êtes pas administrateur.");
        }

        boolean deleted = gatewayService.deleteMatch(id);
        return deleted ? ResponseEntity.ok("Match supprimé.") : ResponseEntity.status(404).body("Match non trouvé.");
    }
}
