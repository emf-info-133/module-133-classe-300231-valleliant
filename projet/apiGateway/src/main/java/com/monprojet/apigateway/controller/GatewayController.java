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
        if (session == null) {
            System.out.println("Pas de session active.");
            return false;
        }

        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            System.out.println("Aucun utilisateur en session.");
            return false;
        }

        if (!(userObj instanceof UserDTO)) {
            System.out.println("L'objet 'user' en session n'est pas un UserDTO.");
            return false;
        }

        UserDTO user = (UserDTO) userObj;
        String email = user.getEmail();
        System.out.println("Utilisateur connecté : " + email); // Log pour vérifier l'email stocké dans la session

        // Log supplémentaire pour inspecter l'email
        if (email == null) {
            System.out.println("L'email de l'utilisateur est null.");
        } else {
            System.out.println("Email de l'utilisateur : " + email);
        }

        return email != null && email.equalsIgnoreCase("admin@admin.com");
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

    @PostMapping("/users")
    @Operation(summary = "Créer un nouvel utilisateur (admin uniquement)")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO, @RequestParam String rawPassword,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }

        // Appel du service avec le UserDTO et le mot de passe brut
        UserDTO createdUser = gatewayService.createUser(userDTO, rawPassword);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Mettre à jour un utilisateur par ID (utilisateur lui-même ou administrateur uniquement)")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }

        HttpSession session = request.getSession(false);
        UserDTO currentUser = (UserDTO) session.getAttribute("user");

        // Si l'utilisateur connecté n'est pas l'administrateur et n'est pas celui qu'il
        // veut mettre à jour
        if (!currentUser.getId().equals(id)) {
            return ResponseEntity.status(403).build(); // Accès interdit
        }

        boolean updated = gatewayService.updateUser(id, userDTO);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---------------- TEAMS ----------------

    @GetMapping("/teams")
    public ResponseEntity<List<TeamDTO>> getAllTeams(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }

        List<TeamDTO> teams = gatewayService.getAllTeams();
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

        TeamDTO created = gatewayService.createTeam(teamDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/teams/{id}")
    @Operation(summary = "Mettre à jour une équipe par ID (utilisateur ayant créé l'équipe ou administrateur uniquement)")
    public ResponseEntity<Void> updateTeam(@PathVariable Integer id, @RequestBody TeamDTO teamDTO,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }

        HttpSession session = request.getSession(false);
        UserDTO currentUser = (UserDTO) session.getAttribute("user");

        // Si l'utilisateur connecté n'est pas l'administrateur et n'est pas celui qui a
        // créé l'équipe
        TeamDTO team = gatewayService.getTeamById(id);
        if (team == null || (!isAdmin(request) || !team.getCaptain().equals(currentUser.getId()))) {
            return ResponseEntity.status(403).build(); // Accès interdit
        }

        boolean updated = gatewayService.updateTeam(id, teamDTO);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }

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
