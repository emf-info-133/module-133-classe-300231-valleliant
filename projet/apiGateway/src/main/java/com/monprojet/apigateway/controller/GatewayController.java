package com.monprojet.apigateway.controller;

import com.monprojet.apigateway.dto.*;
import com.monprojet.apigateway.service.GatewayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    // ---------------- Utilisateurs ----------------

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) return ResponseEntity.status(401).build();

        List<UserDTO> users = gatewayService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) return ResponseEntity.status(401).build();

        UserDTO user = gatewayService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO, @RequestParam String password) {
        UserDTO createdUser = gatewayService.createUser(userDTO, password);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) return ResponseEntity.status(401).build();

        boolean updated = gatewayService.updateUser(id, userDTO);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) return ResponseEntity.status(401).build();

        boolean deleted = gatewayService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---------------- Tournois ----------------

    @GetMapping("/tournaments")
    public ResponseEntity<List<TournamentDTO>> getAllTournaments(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) return ResponseEntity.status(401).build();

        List<TournamentDTO> tournaments = gatewayService.getAllTournaments();
        return tournaments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tournaments);
    }

    @PostMapping("/tournaments")
    public ResponseEntity<TournamentDTO> createTournament(@RequestBody TournamentDTO tournamentDTO, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) return ResponseEntity.status(401).build();

        TournamentDTO createdTournament = gatewayService.createTournament(tournamentDTO);
        return new ResponseEntity<>(createdTournament, HttpStatus.CREATED);
    }

    @PutMapping("/tournaments/{id}")
    public ResponseEntity<Void> updateTournament(@PathVariable Integer id, @RequestBody TournamentDTO tournamentDTO, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) return ResponseEntity.status(401).build();

        boolean updated = gatewayService.updateTournament(id, tournamentDTO);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/tournaments/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) return ResponseEntity.status(401).build();

        boolean deleted = gatewayService.deleteTournament(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---------------- Matchs ----------------

    @GetMapping("/matches")
    public ResponseEntity<List<MatchDTO>> getAllMatches(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) return ResponseEntity.status(401).build();

        List<MatchDTO> matches = gatewayService.getAllMatches();
        return matches.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(matches);
    }

    @PostMapping("/matches")
    public ResponseEntity<MatchDTO> createMatch(@RequestBody MatchDTO matchDTO, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) return ResponseEntity.status(401).build();

        MatchDTO createdMatch = gatewayService.createMatch(matchDTO);
        return new ResponseEntity<>(createdMatch, HttpStatus.CREATED);
    }

    @PutMapping("/matches/{id}")
    public ResponseEntity<Void> updateMatch(@PathVariable Integer id, @RequestBody MatchDTO matchDTO, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) return ResponseEntity.status(401).build();

        boolean updated = gatewayService.updateMatch(id, matchDTO);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/matches/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) return ResponseEntity.status(401).build();

        boolean deleted = gatewayService.deleteMatch(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
