package com.monprojet.apigateway.service;

import com.monprojet.apigateway.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    // -------------------------- UTILISATEURS --------------------------

    public List<UserDTO> getAllUsers() {
        try {
            ResponseEntity<UserDTO[]> response = restTemplate.getForEntity(serviceRest1BaseUrl + "/users",
                    UserDTO[].class);
            return response.getStatusCode().is2xxSuccessful() ? Arrays.asList(response.getBody())
                    : Collections.emptyList();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération des utilisateurs", ex);
        }
    }

    public UserDTO getUserByEmail(String email) {
        try {
            return restTemplate.getForObject(serviceRest1BaseUrl + "/users/email/{email}", UserDTO.class, email);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur avec l'email : " + email, ex);
        }
    }

    public UserDTO getUserById(Integer id) {
        try {
            return restTemplate.getForObject(serviceRest1BaseUrl + "/users/" + id, UserDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur avec ID: " + id, ex);
        }
    }

    public UserDTO createUser(UserDTO userDTO, String rawPassword) {
        try {
            // Hasher le mot de passe avant de l’envoyer au microservice user
            String hashedPassword = new BCryptPasswordEncoder().encode(rawPassword);

            // Construction manuelle du payload à envoyer
            Map<String, String> payload = new HashMap<>();
            payload.put("name", userDTO.getName());
            payload.put("email", userDTO.getEmail());
            payload.put("password", hashedPassword);

            // Appel POST vers le service user
            return restTemplate.postForObject(serviceRest1BaseUrl + "/users", payload, UserDTO.class);

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // Gestion d'erreur claire et traçable
            throw new RuntimeException("Erreur lors de la création de l'utilisateur : " + ex.getResponseBodyAsString(),
                    ex);
        }
    }

    public boolean updateUser(Integer id, UserDTO userDTO) {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("name", userDTO.getName());
            payload.put("email", userDTO.getEmail());

            restTemplate.put(serviceRest1BaseUrl + "/users/" + id, payload);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur", ex);
        }
    }

    public boolean deleteUser(Integer id) {
        try {
            restTemplate.delete(serviceRest1BaseUrl + "/users/" + id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur", ex);
        }
    }

    // -------------------------- EQUIPES --------------------------

    public List<TeamDTO> getTeamsByTournament(Integer tournamentId) {
        try {
            // Appel au service avec paramètre tournamentId
            ResponseEntity<TeamDTO[]> response = restTemplate.getForEntity(
                    serviceRest1BaseUrl + "/teams?tournamentId=" + tournamentId,
                    TeamDTO[].class);
            return response.getStatusCode().is2xxSuccessful()
                    ? Arrays.asList(response.getBody())
                    : Collections.emptyList();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération des équipes pour le tournoi: " + tournamentId,
                    ex);
        }
    }

    public List<TeamDTO> getAllTeams() {
        try {
            ResponseEntity<TeamDTO[]> response = restTemplate.getForEntity(serviceRest1BaseUrl + "/teams",
                    TeamDTO[].class);
            return response.getStatusCode().is2xxSuccessful() ? Arrays.asList(response.getBody())
                    : Collections.emptyList();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération des équipes", ex);
        }
    }

    public TeamDTO getTeamById(Integer id) {
        try {
            return restTemplate.getForObject(serviceRest1BaseUrl + "/teams/" + id, TeamDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération de l'équipe", ex);
        }
    }

    public TeamDTO createTeam(TeamDTO teamDTO) {
        try {
            // Appel au microservice pour créer l'équipe
            return restTemplate.postForObject(serviceRest1BaseUrl + "/teams", teamDTO, TeamDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // Log l'erreur HTTP pour diagnostiquer la cause
            logError(ex);
            throw new RuntimeException("Erreur lors de la création de l'équipe, erreur HTTP: " + ex.getStatusCode(),
                    ex);
        } catch (Exception ex) {
            // Gestion d'autres types d'erreurs comme des erreurs réseau
            logError(ex);
            throw new RuntimeException("Erreur inattendue lors de la création de l'équipe", ex);
        }
    }

    private void logError(Exception ex) {
        // Log pour mieux comprendre l'erreur
        System.err.println("Erreur: " + ex.getMessage());
    }

    public boolean updateTeam(Integer id, TeamDTO teamDTO) {
        try {
            restTemplate.put(serviceRest1BaseUrl + "/teams/" + id, teamDTO);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'équipe", ex);
        }
    }

    public boolean deleteTeam(Integer id) {
        try {
            restTemplate.delete(serviceRest1BaseUrl + "/teams/" + id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la suppression de l'équipe", ex);
        }
    }

    // -------------------------- TOURNOIS --------------------------

    public List<TournamentDTO> getAllTournaments() {
        try {
            ResponseEntity<TournamentDTO[]> response = restTemplate.getForEntity(serviceRest2BaseUrl + "/tournaments",
                    TournamentDTO[].class);
            return response.getStatusCode().is2xxSuccessful() ? Arrays.asList(response.getBody())
                    : Collections.emptyList();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération des tournois", ex);
        }
    }

    public TournamentDTO getTournamentById(Integer id) {
        try {
            return restTemplate.getForObject(serviceRest2BaseUrl + "/tournaments/" + id, TournamentDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération du tournoi", ex);
        }
    }

    public TournamentDTO createTournament(TournamentDTO tournamentDTO) {
        try {
            return restTemplate.postForObject(serviceRest2BaseUrl + "/tournaments", tournamentDTO, TournamentDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la création du tournoi", ex);
        }
    }

    public boolean updateTournament(Integer id, TournamentDTO tournamentDTO) {
        try {
            restTemplate.put(serviceRest2BaseUrl + "/tournaments/" + id, tournamentDTO);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la mise à jour du tournoi", ex);
        }
    }

    public boolean deleteTournament(Integer id) {
        try {
            restTemplate.delete(serviceRest2BaseUrl + "/tournaments/" + id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la suppression du tournoi", ex);
        }
    }

    // -------------------------- MATCHS --------------------------

    public List<MatchDTO> getAllMatches() {
        try {
            ResponseEntity<MatchDTO[]> response = restTemplate.getForEntity(serviceRest2BaseUrl + "/matches",
                    MatchDTO[].class);
            return response.getStatusCode().is2xxSuccessful() ? Arrays.asList(response.getBody())
                    : Collections.emptyList();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération des matchs", ex);
        }
    }

    public MatchDTO getMatchById(Integer id) {
        try {
            return restTemplate.getForObject(serviceRest2BaseUrl + "/matches/" + id, MatchDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération du match", ex);
        }
    }

    public MatchDTO createMatch(MatchDTO matchDTO) {
        try {
            return restTemplate.postForObject(serviceRest2BaseUrl + "/matches", matchDTO, MatchDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la création du match", ex);
        }
    }

    public boolean updateMatch(Integer id, MatchDTO matchDTO) {
        try {
            restTemplate.put(serviceRest2BaseUrl + "/matches/" + id, matchDTO);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la mise à jour du match", ex);
        }
    }

    public boolean deleteMatch(Integer id) {
        try {
            restTemplate.delete(serviceRest2BaseUrl + "/matches/" + id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la suppression du match", ex);
        }
    }

    // -------------------------- JEUX --------------------------

    public List<GameDTO> getAllGames() {
        try {
            ResponseEntity<GameDTO[]> response = restTemplate.getForEntity(serviceRest2BaseUrl + "/games",
                    GameDTO[].class);
            return response.getStatusCode().is2xxSuccessful() ? Arrays.asList(response.getBody())
                    : Collections.emptyList();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération des jeux", ex);
        }
    }

    public GameDTO getGameById(Integer id) {
        try {
            return restTemplate.getForObject(serviceRest2BaseUrl + "/games/" + id, GameDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération du jeu", ex);
        }
    }
}
