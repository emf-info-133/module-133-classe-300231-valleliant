package com.monprojet.apigateway.service;

import com.monprojet.apigateway.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

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

    // -------------------------- Méthodes Utilisateurs --------------------------

    /**
     * Récupérer tous les utilisateurs
     */
    public List<UserDTO> getAllUsers() {
        try {
            ResponseEntity<UserDTO[]> response = restTemplate.getForEntity(serviceRest1BaseUrl + "/users", UserDTO[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Arrays.asList(response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // Loggez les erreurs ici ou retournez une réponse d'erreur appropriée
            throw new RuntimeException("Erreur lors de la récupération des utilisateurs", ex);
        }
        return Collections.emptyList(); // Retourne une liste vide en cas d'erreur
    }

    /**
     * Récupérer un utilisateur par son ID
     */
    public UserDTO getUserById(Integer id) {
        try {
            return restTemplate.getForObject(serviceRest1BaseUrl + "/users/" + id, UserDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur avec ID: " + id, ex);
        }
    }

    /**
     * Créer un utilisateur
     */
    public UserDTO createUser(UserDTO userDTO, String rawPassword) {
        try {
            String hashedPassword = new BCryptPasswordEncoder().encode(rawPassword);
            Map<String, String> payload = new HashMap<>();
            payload.put("name", userDTO.getName());
            payload.put("email", userDTO.getEmail());
            payload.put("password", hashedPassword);

            return restTemplate.postForObject(serviceRest1BaseUrl + "/users", payload, UserDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la création de l'utilisateur", ex);
        }
    }

    /**
     * Mettre à jour un utilisateur
     */
    public boolean updateUser(Integer id, UserDTO userDTO) {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("name", userDTO.getName());
            payload.put("email", userDTO.getEmail());

            restTemplate.put(serviceRest1BaseUrl + "/users/" + id, payload);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur avec ID: " + id, ex);
        }
    }

    /**
     * Supprimer un utilisateur
     */
    public boolean deleteUser(Integer id) {
        try {
            restTemplate.delete(serviceRest1BaseUrl + "/users/" + id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur avec ID: " + id, ex);
        }
    }

    // -------------------------- Méthodes Tournois --------------------------

    /**
     * Récupérer tous les tournois
     */
    public List<TournamentDTO> getAllTournaments() {
        try {
            ResponseEntity<TournamentDTO[]> response = restTemplate.getForEntity(serviceRest2BaseUrl + "/tournaments", TournamentDTO[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Arrays.asList(response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // Loggez les erreurs ici ou retournez une réponse d'erreur appropriée
            throw new RuntimeException("Erreur lors de la récupération des tournois", ex);
        }
        return Collections.emptyList(); // Retourne une liste vide en cas d'erreur
    }

    /**
     * Créer un tournoi
     */
    public TournamentDTO createTournament(TournamentDTO tournamentDTO) {
        try {
            return restTemplate.postForObject(serviceRest2BaseUrl + "/tournaments", tournamentDTO, TournamentDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la création du tournoi", ex);
        }
    }

    /**
     * Mettre à jour un tournoi
     */
    public boolean updateTournament(Integer id, TournamentDTO tournamentDTO) {
        try {
            restTemplate.put(serviceRest2BaseUrl + "/tournaments/" + id, tournamentDTO);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la mise à jour du tournoi avec ID: " + id, ex);
        }
    }

    /**
     * Supprimer un tournoi
     */
    public boolean deleteTournament(Integer id) {
        try {
            restTemplate.delete(serviceRest2BaseUrl + "/tournaments/" + id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la suppression du tournoi avec ID: " + id, ex);
        }
    }

    // -------------------------- Méthodes Matchs --------------------------

    /**
     * Récupérer tous les matchs
     */
    public List<MatchDTO> getAllMatches() {
        try {
            ResponseEntity<MatchDTO[]> response = restTemplate.getForEntity(serviceRest2BaseUrl + "/matches", MatchDTO[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Arrays.asList(response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // Loggez les erreurs ici ou retournez une réponse d'erreur appropriée
            throw new RuntimeException("Erreur lors de la récupération des matchs", ex);
        }
        return Collections.emptyList(); // Retourne une liste vide en cas d'erreur
    }

    /**
     * Créer un match
     */
    public MatchDTO createMatch(MatchDTO matchDTO) {
        try {
            return restTemplate.postForObject(serviceRest2BaseUrl + "/matches", matchDTO, MatchDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la création du match", ex);
        }
    }

    /**
     * Mettre à jour un match
     */
    public boolean updateMatch(Integer id, MatchDTO matchDTO) {
        try {
            restTemplate.put(serviceRest2BaseUrl + "/matches/" + id, matchDTO);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la mise à jour du match avec ID: " + id, ex);
        }
    }

    /**
     * Supprimer un match
     */
    public boolean deleteMatch(Integer id) {
        try {
            restTemplate.delete(serviceRest2BaseUrl + "/matches/" + id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la suppression du match avec ID: " + id, ex);
        }
    }
}
