package com.monprojet.apigateway.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.monprojet.apigateway.dto.*;

@Service
public class GatewayService {

    private final RestTemplate restTemplate;

    // Base URLs des microservices
    @Value("${serviceRest1.base.url}")
    private String serviceRest1BaseUrl; // Ex: http://localhost:8082/api

    @Value("${serviceRest2.base.url}")
    private String serviceRest2BaseUrl; // Ex: http://localhost:8081/api

    public GatewayService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    // --- Appels vers ServiceRest1 (Utilisateurs & Équipes) ---

    public List<UserDTO> getAllUsers() {
        String url = serviceRest1BaseUrl + "/users";
        ResponseEntity<UserDTO[]> response = restTemplate.getForEntity(url, UserDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public UserDTO getUserById(Integer id) {
        String url = serviceRest1BaseUrl + "/users/" + id;
        return restTemplate.getForObject(url, UserDTO.class);
    }

    public List<TeamDTO> getAllTeams() {
        String url = serviceRest1BaseUrl + "/teams";
        ResponseEntity<TeamDTO[]> response = restTemplate.getForEntity(url, TeamDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public TeamDTO getTeamById(Integer id) {
        String url = serviceRest1BaseUrl + "/teams/" + id;
        return restTemplate.getForObject(url, TeamDTO.class);
    }

    // --- Appels vers ServiceRest2 (Tournois, Jeux, Matches) ---

    public List<TournamentDTO> getAllTournaments() {
        String url = serviceRest2BaseUrl + "/tournaments";
        ResponseEntity<TournamentDTO[]> response = restTemplate.getForEntity(url, TournamentDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public TournamentDTO getTournamentById(Integer id) {
        String url = serviceRest2BaseUrl + "/tournaments/" + id;
        return restTemplate.getForObject(url, TournamentDTO.class);
    }

    public TournamentWithAdminDTO getTournamentWithAdmin(Integer tournamentId) {
        // Récupérer le tournoi depuis serviceRest2
        TournamentDTO tournament = restTemplate.getForObject(
            serviceRest2BaseUrl + "/tournaments/" + tournamentId, TournamentDTO.class);
        if(tournament == null) {
            return null;
        }
        // Récupérer l'administrateur associé depuis serviceRest1
        UserDTO admin = restTemplate.getForObject(
            serviceRest1BaseUrl + "/users/" + tournament.getAdminId(), UserDTO.class);
        
        return new TournamentWithAdminDTO(
            tournament.getId(),
            tournament.getName(),
            tournament.getDate(),
            tournament.getAdminId(),
            (admin != null ? admin.getName() : null),
            (admin != null ? admin.getEmail() : null)
        );
    }

    public List<GameDTO> getAllGames() {
        String url = serviceRest2BaseUrl + "/games";
        ResponseEntity<GameDTO[]> response = restTemplate.getForEntity(url, GameDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public List<MatchDTO> getAllMatches() {
        String url = serviceRest2BaseUrl + "/matches";
        ResponseEntity<MatchDTO[]> response = restTemplate.getForEntity(url, MatchDTO[].class);
        return Arrays.asList(response.getBody());
    }
    
    public List<MatchDTO> getMatchesByTeam(Integer teamId) {
        String url = serviceRest2BaseUrl + "/matches/team/" + teamId;
        ResponseEntity<MatchDTO[]> response = restTemplate.getForEntity(url, MatchDTO[].class);
        return Arrays.asList(response.getBody());
    }
}
