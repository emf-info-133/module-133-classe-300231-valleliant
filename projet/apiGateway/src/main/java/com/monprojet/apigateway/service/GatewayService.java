package com.monprojet.apigateway.service;

import com.monprojet.apigateway.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@Service
public class GatewayService {

    private final RestTemplate restTemplate;

    @Value("${serviceRest1.base.url}")
    private String serviceRest1BaseUrl;

    @Value("${serviceRest2.base.url}")
    private String serviceRest2BaseUrl;

    public GatewayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Vérifie si un utilisateur est déjà connecté
    private final Set<String> loggedInUsers = new HashSet<>(); // Liste des utilisateurs connectés

    public boolean isUserLoggedIn(String username) {
        return loggedInUsers.contains(username);
    }

    public void userLoggedIn(String username) {
        loggedInUsers.add(username);
    }

    public void userLoggedOut(String username) {
        loggedInUsers.remove(username);
    }

    // --- Méthodes pour ServiceRest1 (Utilisateurs et Équipes) ---

    public List<UserDTO> getAllUsers() {
        ResponseEntity<UserDTO[]> response = restTemplate.getForEntity(serviceRest1BaseUrl + "/users", UserDTO[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
    }

    public UserDTO getUserById(Integer id) {
        return restTemplate.getForObject(serviceRest1BaseUrl + "/users/" + id, UserDTO.class);
    }

    public UserDTO createUser(UserDTO userDTO, String rawPassword) {
        String hashedPassword = new BCryptPasswordEncoder().encode(rawPassword);

        Map<String, String> payload = new HashMap<>();
        payload.put("name", userDTO.getName());
        payload.put("email", userDTO.getEmail());
        payload.put("password", hashedPassword);

        return restTemplate.postForObject(serviceRest1BaseUrl + "/users", payload, UserDTO.class);
    }

    public List<TeamDTO> getAllTeams() {
        ResponseEntity<TeamDTO[]> response = restTemplate.getForEntity(serviceRest1BaseUrl + "/teams", TeamDTO[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
    }

    public TeamDTO getTeamById(Integer id) {
        return restTemplate.getForObject(serviceRest1BaseUrl + "/teams/" + id, TeamDTO.class);
    }

    // --- Méthodes pour ServiceRest2 (Tournois, Jeux, Matches) ---

    public List<TournamentDTO> getAllTournaments() {
        ResponseEntity<TournamentDTO[]> response = restTemplate.getForEntity(serviceRest2BaseUrl + "/tournaments",
                TournamentDTO[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
    }

    public TournamentDTO getTournamentById(Integer id) {
        return restTemplate.getForObject(serviceRest2BaseUrl + "/tournaments/" + id, TournamentDTO.class);
    }

    public TournamentDTO createTournament(TournamentDTO tournamentDTO) {
        return restTemplate.postForObject(serviceRest2BaseUrl + "/tournaments", tournamentDTO, TournamentDTO.class);
    }

    public TournamentDTO updateTournament(Integer id, TournamentDTO tournamentDTO) {
        restTemplate.put(serviceRest2BaseUrl + "/tournaments/" + id, tournamentDTO);
        return tournamentDTO; // Retourne l'objet mis à jour
    }

    public void deleteTournament(Integer id) {
        restTemplate.delete(serviceRest2BaseUrl + "/tournaments/" + id);
    }

    public List<GameDTO> getAllGames() {
        ResponseEntity<GameDTO[]> response = restTemplate.getForEntity(serviceRest2BaseUrl + "/games", GameDTO[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
    }

    public List<MatchDTO> getAllMatches() {
        ResponseEntity<MatchDTO[]> response = restTemplate.getForEntity(serviceRest2BaseUrl + "/matches",
                MatchDTO[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
    }

    public List<MatchDTO> getMatchesByTeam(Integer teamId) {
        ResponseEntity<MatchDTO[]> response = restTemplate.getForEntity(serviceRest2BaseUrl + "/matches/team/" + teamId,
                MatchDTO[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : Collections.emptyList();
    }

    public MatchDTO createMatch(MatchDTO matchDTO) {
        return restTemplate.postForObject(serviceRest2BaseUrl + "/matches", matchDTO, MatchDTO.class);
    }

    public MatchDTO updateMatch(Integer id, MatchDTO matchDTO) {
        restTemplate.put(serviceRest2BaseUrl + "/matches/" + id, matchDTO);
        return matchDTO; // Retourne l'objet mis à jour
    }

    public void deleteMatch(Integer id) {
        restTemplate.delete(serviceRest2BaseUrl + "/matches/" + id);
    }

    public TournamentWithAdminDTO getTournamentWithAdmin(Integer tournamentId) {
        TournamentDTO tournament = getTournamentById(tournamentId);
        UserDTO admin = getUserById(tournament.getAdminId());

        return new TournamentWithAdminDTO(
                tournament.getId(),
                tournament.getName(),
                tournament.getDate(),
                tournament.getAdminId(),
                admin != null ? admin.getName() : null,
                admin != null ? admin.getEmail() : null);
    }
}
