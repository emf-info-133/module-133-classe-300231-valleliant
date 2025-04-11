package com.monprojet.apigateway.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.monprojet.apigateway.dto.GameDTO;
import com.monprojet.apigateway.dto.MatchDTO;
import com.monprojet.apigateway.dto.TeamDTO;
import com.monprojet.apigateway.dto.TournamentDTO;
import com.monprojet.apigateway.dto.TournamentWithAdminDTO;
import com.monprojet.apigateway.dto.UserDTO;

@Service
public class GatewayService {

    private final RestTemplate restTemplate;

    // Pour les appels vers ServiceRest1 et ServiceRest2
    @Value("${serviceRest1.base.url}")
    private String serviceRest1BaseUrl; // ex: http://localhost:8082/api

    @Value("${serviceRest2.base.url}")
    private String serviceRest2BaseUrl; // ex: http://localhost:8081/api

    // Dépendances pour l'inscription locale
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructeur injectant le RestTemplate ainsi que le BCryptPasswordEncoder
    public GatewayService(RestTemplate restTemplate,
            BCryptPasswordEncoder passwordEncoder) {
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    // Méthode pour envoyer une requête HTTP
    public void callExternalService() {
        String url = "https://api.externe.com";
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);
    }

    // --- Opérations pour ServiceRest1 (Utilisateurs & Équipes) ---
    // Récupérer tous les utilisateurs via microservice
    public List<UserDTO> getAllUsers() {
        String url = serviceRest1BaseUrl + "/users";
        ResponseEntity<UserDTO[]> response = restTemplate.getForEntity(url, UserDTO[].class);
        return Arrays.asList(response.getBody());
    }

    // Récupérer un utilisateur par ID via microservice
    public UserDTO getUserById(Integer id) {
        String url = serviceRest1BaseUrl + "/users/" + id;
        return restTemplate.getForObject(url, UserDTO.class);
    }

    // Créer un nouvel utilisateur en effectuant une inscription localement (avec
    // hashage du password)
    public UserDTO createUser(UserDTO userDTO, String rawPassword) {
        // Hacher le mot de passe avant de l'envoyer
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // Créer un payload avec l'utilisateur et son mot de passe haché
        Map<String, String> payload = new HashMap<>();
        payload.put("name", userDTO.getName());
        payload.put("email", userDTO.getEmail());
        payload.put("password", hashedPassword);

        // Appeler le service distant pour créer l'utilisateur
        return restTemplate.postForObject(serviceRest1BaseUrl + "/users", payload, UserDTO.class);
    }

    // Mettre à jour un utilisateur via microservice
    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        String url = serviceRest1BaseUrl + "/users/" + id;
        restTemplate.put(url, userDTO);
        return getUserById(id);
    }

    // Supprimer un utilisateur via microservice
    public boolean deleteUser(Integer id) {
        try {
            restTemplate.delete(serviceRest1BaseUrl + "/users/" + id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Pour les équipes
    public List<TeamDTO> getAllTeams() {
        String url = serviceRest1BaseUrl + "/teams";
        ResponseEntity<TeamDTO[]> response = restTemplate.getForEntity(url, TeamDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public TeamDTO getTeamById(Integer id) {
        String url = serviceRest1BaseUrl + "/teams/" + id;
        return restTemplate.getForObject(url, TeamDTO.class);
    }

    public TeamDTO createTeam(TeamDTO teamDTO) {
        return restTemplate.postForObject(serviceRest1BaseUrl + "/teams", teamDTO, TeamDTO.class);
    }

    public TeamDTO updateTeam(Integer id, TeamDTO teamDTO) {
        String url = serviceRest1BaseUrl + "/teams/" + id;
        restTemplate.put(url, teamDTO);
        return getTeamById(id);
    }

    public boolean deleteTeam(Integer id) {
        try {
            restTemplate.delete(serviceRest1BaseUrl + "/teams/" + id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --- Opérations pour ServiceRest2 (Tournois, Jeux, Matches) ---
    public List<TournamentDTO> getAllTournaments() {
        String url = serviceRest2BaseUrl + "/tournaments";
        ResponseEntity<TournamentDTO[]> response = restTemplate.getForEntity(url, TournamentDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public TournamentDTO getTournamentById(Integer id) {
        String url = serviceRest2BaseUrl + "/tournaments/" + id;
        return restTemplate.getForObject(url, TournamentDTO.class);
    }

    public TournamentDTO createTournament(TournamentDTO tournamentDTO) {
        return restTemplate.postForObject(serviceRest2BaseUrl + "/tournaments", tournamentDTO, TournamentDTO.class);
    }

    public TournamentDTO updateTournament(Integer id, TournamentDTO tournamentDTO) {
        String url = serviceRest2BaseUrl + "/tournaments/" + id;
        restTemplate.put(url, tournamentDTO);
        return getTournamentById(id);
    }

    public boolean deleteTournament(Integer id) {
        try {
            restTemplate.delete(serviceRest2BaseUrl + "/tournaments/" + id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Récupérer tournoi avec administrateur (détails supplémentaires)
    public TournamentWithAdminDTO getTournamentWithAdmin(Integer tournamentId) {
        TournamentDTO tournament = restTemplate.getForObject(
                serviceRest2BaseUrl + "/tournaments/" + tournamentId, TournamentDTO.class);
        if (tournament == null) {
            return null;
        }
        UserDTO admin = restTemplate.getForObject(
                serviceRest1BaseUrl + "/users/" + tournament.getAdminId(), UserDTO.class);
        return new TournamentWithAdminDTO(
                tournament.getId(),
                tournament.getName(),
                tournament.getDate(),
                tournament.getAdminId(),
                (admin != null ? admin.getName() : null),
                (admin != null ? admin.getEmail() : null));
    }

    // Opérations sur les jeux
    public List<GameDTO> getAllGames() {
        String url = serviceRest2BaseUrl + "/games";
        ResponseEntity<GameDTO[]> response = restTemplate.getForEntity(url, GameDTO[].class);
        return Arrays.asList(response.getBody());
    }

    public GameDTO createGame(GameDTO gameDTO) {
        return restTemplate.postForObject(serviceRest2BaseUrl + "/games", gameDTO, GameDTO.class);
    }

    public GameDTO updateGame(Integer id, GameDTO gameDTO) {
        String url = serviceRest2BaseUrl + "/games/" + id;
        restTemplate.put(url, gameDTO);
        return restTemplate.getForObject(url, GameDTO.class);
    }

    public boolean deleteGame(Integer id) {
        try {
            restTemplate.delete(serviceRest2BaseUrl + "/games/" + id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Opérations sur les matchs
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

    public MatchDTO createMatch(MatchDTO matchDTO) {
        return restTemplate.postForObject(serviceRest2BaseUrl + "/matches", matchDTO, MatchDTO.class);
    }

    public MatchDTO updateMatch(Integer id, MatchDTO matchDTO) {
        String url = serviceRest2BaseUrl + "/matches/" + id;
        restTemplate.put(url, matchDTO);
        return restTemplate.getForObject(url, MatchDTO.class);
    }

    public boolean deleteMatch(Integer id) {
        try {
            restTemplate.delete(serviceRest2BaseUrl + "/matches/" + id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
