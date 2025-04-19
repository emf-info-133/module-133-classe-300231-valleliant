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
import java.util.Optional;

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
        if (session == null) {
            return false;
        }

        Object userObj = session.getAttribute("user");
        if (userObj == null || !(userObj instanceof UserDTO)) {
            return false;
        }

        UserDTO user = (UserDTO) userObj;
        return user.isAdmin(); // Vérification du rôle administrateur
    }

    // --- USERS ---

    @GetMapping("/users")
    @Operation(summary = "Obtenir tous les utilisateurs")
    public ResponseEntity<List<UserDTO>> getUsers(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        List<UserDTO> users = gatewayService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Obtenir un utilisateur par ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        UserDTO user = gatewayService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Mettre à jour un utilisateur par ID (utilisateur lui-même ou administrateur uniquement)")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }

        HttpSession session = request.getSession(false);
        String identifiant = (String) session.getAttribute("user");

        UserDTO currentUser = gatewayService.findUserByEmailOrName(identifiant);

        // Vérifier si l'utilisateur est l'administrateur ou l'utilisateur lui-même
        if (!(currentUser.isAdmin() || currentUser.getId().equals(id))) {
            return ResponseEntity.status(403).build(); // Accès interdit
        }

        // Mise à jour de l'utilisateur
        boolean updated = gatewayService.updateUser(id, userDTO);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---------------- TEAMS ----------------

    @GetMapping("/teams")
    public ResponseEntity<List<TeamDTO>> getAllTeams(
            @RequestParam(required = false) Integer tournamentId,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }

        List<TeamDTO> teams;
        if (tournamentId != null) {
            // Si un tournamentId est fourni, filtrer les équipes

            teams = gatewayService.getTeamsByTournament(tournamentId);
        } else {
            // Sinon, récupérer toutes les équipes
            teams = gatewayService.getAllTeams();
        }

        return teams.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(teams);
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        TeamDTO team = gatewayService.getTeamById(id);
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }

    @PostMapping("/teams")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }

        // Récupérer l'utilisateur connecté
        HttpSession session = request.getSession(false);
        String identifiant = (String) session.getAttribute("user");

        UserDTO currentUser = gatewayService.findUserByEmailOrName(identifiant);

        // Définir l'utilisateur connecté comme capitaine si ce n'est pas déjà le cas
        if (teamDTO.getCaptain() == null) {
            teamDTO.setCaptain(currentUser.getId());
        }

        TeamDTO created = gatewayService.createTeam(teamDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/teams/{id}")
    @Operation(summary = "Mettre à jour une équipe par ID (utilisateur ayant créé l'équipe ou administrateur uniquement)")
    public ResponseEntity<Void> updateTeam(
            @PathVariable Integer id,
            @RequestBody TeamDTO teamDTO,
            HttpServletRequest request) {

        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }

        HttpSession session = request.getSession(false);
        String identifiant = (String) session.getAttribute("user");

        UserDTO currentUser = gatewayService.findUserByEmailOrName(identifiant);

        // Récupérer l’équipe actuelle
        TeamDTO existingTeam = gatewayService.getTeamById(id);
        if (existingTeam == null) {
            return ResponseEntity.notFound().build(); // L’équipe n’existe pas
        }

        // Vérifier si l’utilisateur est autorisé à modifier l’équipe
        if (!(isAdmin(request) || existingTeam.getCaptain().equals(currentUser.getId()))) {
            return ResponseEntity.status(403).build(); // Non autorisé
        }

        // Fallback sur le capitaine s’il est manquant
        if (teamDTO.getCaptain() == null) {
            teamDTO.setCaptain(currentUser.getId());
        }

        // Sécurité basique : éviter d’envoyer un DTO incomplet
        if (teamDTO.getName() == null || teamDTO.getName().trim().isEmpty()
                || teamDTO.getTournament() == null) {
            return ResponseEntity.badRequest().build(); // Mauvaise requête
        }

        // Exécution de la mise à jour
        try {
            boolean updated = gatewayService.updateTeam(id, teamDTO);
            return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception ex) {
            System.err.println("Erreur lors du PUT : " + ex.getMessage());
            return ResponseEntity.status(500).build(); // Erreur serveur
        }
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }

        // Récupérer l’équipe actuelle
        TeamDTO existingTeam = gatewayService.getTeamById(id);
        if (existingTeam == null) {
            return ResponseEntity.notFound().build(); // L’équipe n’existe pas
        }

        HttpSession session = request.getSession(false);
        String identifiant = (String) session.getAttribute("user");

        UserDTO currentUser = gatewayService.findUserByEmailOrName(identifiant);

        if (currentUser == null) {
            return ResponseEntity.status(401).build(); // Aucun utilisateur trouvé
        }

        // Vérifier si l’utilisateur est administrateur ou capitaine de l’équipe
        if (!(isAdmin(request) || existingTeam.getCaptain().equals(currentUser.getId()))) {
            return ResponseEntity.status(403).build(); // Non autorisé
        }

        // Suppression de l’équipe
        boolean deleted = gatewayService.deleteTeam(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---------------- TOURNAMENTS ----------------

    @GetMapping("/tournaments")
    public ResponseEntity<List<TournamentDTO>> getAllTournaments(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        List<TournamentDTO> tournaments = gatewayService.getAllTournaments();
        return tournaments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tournaments);
    }

    @GetMapping("/tournaments/{id}")
    public ResponseEntity<TournamentDTO> getTournamentById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        TournamentDTO tournament = gatewayService.getTournamentById(id);
        return tournament != null ? ResponseEntity.ok(tournament) : ResponseEntity.notFound().build();
    }

    @PostMapping("/tournaments")
    public ResponseEntity<TournamentDTO> createTournament(@RequestBody TournamentDTO dto, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }
        TournamentDTO created = gatewayService.createTournament(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/tournaments/{id}")
    public ResponseEntity<Void> updateTournament(@PathVariable Integer id, @RequestBody TournamentDTO dto,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }
        boolean updated = gatewayService.updateTournament(id, dto);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/tournaments/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }
        boolean deleted = gatewayService.deleteTournament(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---------------- MATCHES ----------------

    @GetMapping("/matches")
    public ResponseEntity<List<MatchDTO>> getAllMatches(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        List<MatchDTO> matches = gatewayService.getAllMatches();
        return matches.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(matches);
    }

    @GetMapping("/matches/{id}")
    public ResponseEntity<MatchDTO> getMatchById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        MatchDTO match = gatewayService.getMatchById(id);
        return match != null ? ResponseEntity.ok(match) : ResponseEntity.notFound().build();
    }

    @PostMapping("/matches")
    public ResponseEntity<MatchDTO> createMatch(@RequestBody MatchDTO dto, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }
        MatchDTO created = gatewayService.createMatch(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/matches/{id}")
    public ResponseEntity<Void> updateMatch(@PathVariable Integer id, @RequestBody MatchDTO dto,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }
        boolean updated = gatewayService.updateMatch(id, dto);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/matches/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }
        boolean deleted = gatewayService.deleteMatch(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---------------- GAMES (optionnel) ----------------

    @GetMapping("/games")
    public ResponseEntity<List<GameDTO>> getGames(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        List<GameDTO> games = gatewayService.getAllGames();
        return games.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(games);
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        GameDTO game = gatewayService.getGameById(id);
        return game != null ? ResponseEntity.ok(game) : ResponseEntity.notFound().build();
    }
}
