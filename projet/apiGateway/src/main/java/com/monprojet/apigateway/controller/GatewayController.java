package com.monprojet.apigateway.controller;

import com.monprojet.apigateway.dto.*;
import com.monprojet.apigateway.service.GatewayService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// ... imports inchangés ...

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final GatewayService gatewayService;

    @Autowired
    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    private boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return false;

        Object userObj = session.getAttribute("user");
        if (userObj instanceof UserDTO user) {
            return "admin@admin.com".equalsIgnoreCase(user.getEmail());
        }
        return false;
    }

    // --- USERS ---

    @GetMapping("/users")
    @Operation(summary = "Obtenir tous les utilisateurs")
    public ResponseEntity<List<UserDTO>> getUsers(HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();

        List<UserDTO> users = gatewayService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Obtenir un utilisateur par ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();

        UserDTO user = gatewayService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/users")
    @Operation(summary = "Créer un nouvel utilisateur (admin uniquement)")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO, @RequestParam String rawPassword,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }

        // Appel du service avec le UserDTO et le mot de passe brut
        UserDTO createdUser = gatewayService.createUser(userDTO, rawPassword);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Mettre à jour un utilisateur par ID (admin uniquement)")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();
        if (!isAdmin(request))
            return ResponseEntity.status(403).build();

        boolean updated = gatewayService.updateUser(id, userDTO);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Supprimer un utilisateur par ID (admin uniquement)")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();
        if (!isAdmin(request))
            return ResponseEntity.status(403).build();

        boolean deleted = gatewayService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---------------- TEAMS ----------------

    @GetMapping("/teams")
    public ResponseEntity<List<TeamDTO>> getAllTeams(HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();

        List<TeamDTO> teams = gatewayService.getAllTeams();
        return teams.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(teams);
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();

        TeamDTO team = gatewayService.getTeamById(id);
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }

    @PostMapping("/teams")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO, HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();
        if (!isAdmin(request))
            return ResponseEntity.status(403).build();

        TeamDTO created = gatewayService.createTeam(teamDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/teams/{id}")
    public ResponseEntity<Void> updateTeam(@PathVariable Integer id, @RequestBody TeamDTO teamDTO,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();
        if (!isAdmin(request))
            return ResponseEntity.status(403).build();

        boolean updated = gatewayService.updateTeam(id, teamDTO);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();
        if (!isAdmin(request))
            return ResponseEntity.status(403).build();

        boolean deleted = gatewayService.deleteTeam(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---------------- TOURNAMENTS ----------------

    @GetMapping("/tournaments")
    public ResponseEntity<List<TournamentDTO>> getAllTournaments(HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();

        List<TournamentDTO> tournaments = gatewayService.getAllTournaments();
        return tournaments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tournaments);
    }

    @GetMapping("/tournaments/{id}")
    public ResponseEntity<TournamentDTO> getTournamentById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();

        TournamentDTO tournament = gatewayService.getTournamentById(id);
        return tournament != null ? ResponseEntity.ok(tournament) : ResponseEntity.notFound().build();
    }

    @PostMapping("/tournaments")
    public ResponseEntity<TournamentDTO> createTournament(@RequestBody TournamentDTO dto, HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();
        if (!isAdmin(request))
            return ResponseEntity.status(403).build();

        TournamentDTO created = gatewayService.createTournament(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/tournaments/{id}")
    public ResponseEntity<Void> updateTournament(@PathVariable Integer id, @RequestBody TournamentDTO dto,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();
        if (!isAdmin(request))
            return ResponseEntity.status(403).build();

        boolean updated = gatewayService.updateTournament(id, dto);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/tournaments/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();
        if (!isAdmin(request))
            return ResponseEntity.status(403).build();

        boolean deleted = gatewayService.deleteTournament(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---------------- MATCHES ----------------

    @GetMapping("/matches")
    public ResponseEntity<List<MatchDTO>> getAllMatches(HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();

        List<MatchDTO> matches = gatewayService.getAllMatches();
        return matches.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(matches);
    }

    @GetMapping("/matches/{id}")
    public ResponseEntity<MatchDTO> getMatchById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();

        MatchDTO match = gatewayService.getMatchById(id);
        return match != null ? ResponseEntity.ok(match) : ResponseEntity.notFound().build();
    }

    @PostMapping("/matches")
    public ResponseEntity<MatchDTO> createMatch(@RequestBody MatchDTO dto, HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();
        if (!isAdmin(request))
            return ResponseEntity.status(403).build();

        MatchDTO created = gatewayService.createMatch(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/matches/{id}")
    public ResponseEntity<Void> updateMatch(@PathVariable Integer id, @RequestBody MatchDTO dto,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();
        if (!isAdmin(request))
            return ResponseEntity.status(403).build();

        boolean updated = gatewayService.updateMatch(id, dto);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/matches/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();
        if (!isAdmin(request))
            return ResponseEntity.status(403).build();

        boolean deleted = gatewayService.deleteMatch(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---------------- GAMES (optionnel) ----------------

    @GetMapping("/games")
    public ResponseEntity<List<GameDTO>> getGames(HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();

        List<GameDTO> games = gatewayService.getAllGames();
        return games.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(games);
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request))
            return ResponseEntity.status(401).build();

        GameDTO game = gatewayService.getGameById(id);
        return game != null ? ResponseEntity.ok(game) : ResponseEntity.notFound().build();
    }
}
