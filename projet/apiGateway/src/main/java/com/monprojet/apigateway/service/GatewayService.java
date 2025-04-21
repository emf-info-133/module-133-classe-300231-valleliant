package com.monprojet.apigateway.service;

import com.monprojet.apigateway.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

    // -------------------------- UTILISATEURS --------------------------

    /**
     * Récupère tous les utilisateurs.
     * 
     * @return Une liste de tous les utilisateurs. Si aucune donnée n'est trouvée,
     *         une liste vide est retournée.
     * @throws RuntimeException si une erreur survient lors de la récupération des
     *                          utilisateurs.
     */
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

    /**
     * Récupère un utilisateur par son nom.
     * 
     * @param name Le nom de l'utilisateur.
     * @return L'utilisateur correspondant.
     * @throws RuntimeException si une erreur survient lors de la récupération de
     *                          l'utilisateur.
     */
    public UserDTO getUserByName(String name) {
        return restTemplate.getForObject(serviceRest1BaseUrl + "/users/name/{name}", UserDTO.class, name);
    }

    /**
     * Récupère un utilisateur par son adresse email.
     * 
     * @param email L'email de l'utilisateur.
     * @return L'utilisateur correspondant.
     * @throws RuntimeException si une erreur survient lors de la récupération de
     *                          l'utilisateur.
     */
    public UserDTO getUserByEmail(String email) {
        return restTemplate.getForObject(serviceRest1BaseUrl + "/users/email/{email}", UserDTO.class, email);
    }

    /**
     * Recherche un utilisateur par email ou nom d'utilisateur.
     * Si l'email est introuvable, la méthode tente de récupérer l'utilisateur par
     * son nom.
     * 
     * @param identifiant L'email ou le nom de l'utilisateur.
     * @return L'utilisateur correspondant.
     * @throws RuntimeException si l'utilisateur n'est pas trouvé avec l'identifiant
     *                          fourni.
     */
    public UserDTO findUserByEmailOrName(String identifiant) {
        try {
            return getUserByEmail(identifiant);
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Email non trouvé, tentative avec le nom d'utilisateur...");
            return getUserByName(identifiant);
        }
    }

    /**
     * Récupère un utilisateur par son ID.
     * 
     * @param id L'ID de l'utilisateur.
     * @return L'utilisateur correspondant.
     * @throws RuntimeException si une erreur survient lors de la récupération de
     *                          l'utilisateur.
     */
    public UserDTO getUserById(Integer id) {
        try {
            return restTemplate.getForObject(serviceRest1BaseUrl + "/users/" + id, UserDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur avec ID: " + id, ex);
        }
    }

    /**
     * Crée un nouvel utilisateur.
     * 
     * @param userDTO     L'objet représentant l'utilisateur à créer.
     * @param rawPassword Le mot de passe en texte brut de l'utilisateur.
     * @return L'utilisateur créé avec ses informations, y compris l'ID généré.
     * @throws RuntimeException si une erreur survient lors de la création de
     *                          l'utilisateur.
     */
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

    /**
     * Met à jour les informations d'un utilisateur.
     * 
     * @param id      L'ID de l'utilisateur à mettre à jour.
     * @param userDTO L'objet contenant les nouvelles informations de l'utilisateur.
     * @return true si la mise à jour a réussi, false sinon.
     * @throws RuntimeException si une erreur survient lors de la mise à jour de
     *                          l'utilisateur.
     */
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

    /**
     * Supprime un utilisateur.
     * 
     * @param id L'ID de l'utilisateur à supprimer.
     * @return true si la suppression a réussi, false sinon.
     * @throws RuntimeException si une erreur survient lors de la suppression de
     *                          l'utilisateur.
     */
    public boolean deleteUser(Integer id) {
        try {
            restTemplate.delete(serviceRest1BaseUrl + "/users/" + id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur", ex);
        }
    }

    // -------------------------- EQUIPES --------------------------

    /**
     * Récupère la liste des équipes pour un tournoi donné.
     * 
     * @param tournamentId L'ID du tournoi pour lequel récupérer les équipes.
     * @return Une liste d'équipes correspondant au tournoi. Si aucune équipe n'est
     *         trouvée, une liste vide est retournée.
     * @throws RuntimeException si une erreur survient lors de la récupération des
     *                          équipes pour le tournoi.
     */
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

    /**
     * Récupère toutes les équipes disponibles.
     * 
     * @return Une liste de toutes les équipes. Si aucune équipe n'est trouvée, une
     *         liste vide est retournée.
     * @throws RuntimeException si une erreur survient lors de la récupération des
     *                          équipes.
     */
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

    /**
     * Récupère une équipe par son ID.
     * 
     * @param id L'ID de l'équipe à récupérer.
     * @return L'équipe correspondant à l'ID donné.
     * @throws RuntimeException si une erreur survient lors de la récupération de
     *                          l'équipe.
     */
    public TeamDTO getTeamById(Integer id) {
        try {
            return restTemplate.getForObject(serviceRest1BaseUrl + "/teams/" + id, TeamDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération de l'équipe", ex);
        }
    }

    /**
     * Crée une nouvelle équipe.
     * 
     * @param teamDTO L'objet représentant l'équipe à créer.
     * @return L'équipe créée avec ses informations.
     * @throws RuntimeException si une erreur survient lors de la création de
     *                          l'équipe ou si le tournoi spécifié n'existe pas.
     */
    public TeamDTO createTeam(TeamDTO teamDTO) {
        // Vérifier si le tournoi existe avant de créer l'équipe
        if (!isTournamentExists(teamDTO.getTournament())) {
            throw new RuntimeException("Impossible de créer l'équipe : le tournoi spécifié n'existe pas.");
        }

        try {
            return restTemplate.postForObject(serviceRest1BaseUrl + "/teams", teamDTO, TeamDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            logError(ex);
            throw new RuntimeException("Erreur lors de la création de l'équipe, erreur HTTP: " + ex.getStatusCode(),
                    ex);
        } catch (Exception ex) {
            logError(ex);
            throw new RuntimeException("Erreur inattendue lors de la création de l'équipe", ex);
        }
    }

    /**
     * Log l'erreur pour une meilleure compréhension de l'exception.
     * 
     * @param ex L'exception à loguer.
     */
    private void logError(Exception ex) {
        // Log pour mieux comprendre l'erreur
        System.err.println("Erreur: " + ex.getMessage());
    }

    /**
     * Met à jour les informations d'une équipe.
     * 
     * @param id      L'ID de l'équipe à mettre à jour.
     * @param teamDTO L'objet contenant les nouvelles informations de l'équipe.
     * @return true si la mise à jour a réussi, false sinon.
     * @throws RuntimeException si une erreur survient lors de la mise à jour de
     *                          l'équipe ou si le tournoi spécifié n'existe pas.
     */
    public boolean updateTeam(Integer id, TeamDTO teamDTO) {
        // Vérifier si le tournoi existe avant de mettre à jour l'équipe
        if (!isTournamentExists(teamDTO.getTournament())) {
            throw new RuntimeException("Impossible de mettre à jour l'équipe : le tournoi spécifié n'existe pas.");
        }

        try {
            restTemplate.put(serviceRest1BaseUrl + "/teams/" + id, teamDTO);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'équipe", ex);
        }
    }

    /**
     * Supprime une équipe par son ID.
     * 
     * @param id L'ID de l'équipe à supprimer.
     * @return true si la suppression a réussi, false sinon.
     * @throws RuntimeException si une erreur survient lors de la suppression de
     *                          l'équipe.
     */
    public boolean deleteTeam(Integer id) {
        try {
            restTemplate.delete(serviceRest1BaseUrl + "/teams/" + id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la suppression de l'équipe", ex);
        }
    }

    // -------------------------- TEAM-USERS --------------------------

    /**
     * Permet à un utilisateur de rejoindre une équipe.
     * 
     * @param teamUserDTO L'objet représentant l'utilisateur et l'équipe qu'il
     *                    souhaite rejoindre.
     * @return L'objet TeamUserDTO représentant la relation créée entre
     *         l'utilisateur et l'équipe.
     * @throws RuntimeException si une erreur survient lors de la demande pour
     *                          rejoindre l'équipe.
     */
    public TeamUserDTO joinTeam(TeamUserDTO teamUserDTO) {
        String url = serviceRest1BaseUrl + "/team-users/join";
        try {
            return restTemplate.postForObject(url, teamUserDTO, TeamUserDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la demande de rejoindre une équipe", ex);
        }
    }

    /**
     * Permet à un utilisateur de quitter une équipe.
     * 
     * @param teamUserDTO L'objet représentant l'utilisateur et l'équipe qu'il
     *                    souhaite quitter.
     * @return true si la demande pour quitter l'équipe a été effectuée avec succès,
     *         false sinon.
     * @throws RuntimeException si une erreur survient lors de la demande pour
     *                          quitter l'équipe.
     */
    public boolean leaveTeam(TeamUserDTO teamUserDTO) {
        String url = serviceRest1BaseUrl + "/team-users/leave";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<TeamUserDTO> request = new HttpEntity<>(teamUserDTO, headers);

            restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    request,
                    Void.class);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la demande de quitter une équipe", ex);
        }
    }

    // -------------------------- TOURNOIS --------------------------

    /**
     * Vérifie si un tournoi existe en fonction de son identifiant.
     * 
     * @param tournamentId L'ID du tournoi à vérifier.
     * @return true si le tournoi existe, false sinon.
     */
    private boolean isTournamentExists(Integer tournamentId) {
        try {
            ResponseEntity<TournamentDTO> response = restTemplate.getForEntity(
                    serviceRest2BaseUrl + "/tournaments/" + tournamentId, TournamentDTO.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return false;
        }
    }

    /**
     * Récupère la liste de tous les tournois.
     * 
     * @return Une liste contenant tous les tournois disponibles.
     * @throws RuntimeException si une erreur survient lors de la récupération des
     *                          tournois.
     */
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

    /**
     * Récupère un tournoi par son identifiant.
     * 
     * @param id L'ID du tournoi à récupérer.
     * @return L'objet TournamentDTO représentant le tournoi avec l'ID donné.
     * @throws RuntimeException si une erreur survient lors de la récupération du
     *                          tournoi.
     */
    public TournamentDTO getTournamentById(Integer id) {
        try {
            return restTemplate.getForObject(serviceRest2BaseUrl + "/tournaments/" + id, TournamentDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération du tournoi", ex);
        }
    }

    /**
     * Crée un tournoi avec les informations spécifiées.
     * 
     * @param tournamentDTO L'objet TournamentDTO contenant les informations du
     *                      tournoi à créer.
     * @return L'objet TournamentDTO représentant le tournoi créé.
     * @throws RuntimeException si une erreur survient lors de la création du
     *                          tournoi.
     */
    public TournamentDTO createTournament(TournamentDTO tournamentDTO) {
        try {
            return restTemplate.postForObject(serviceRest2BaseUrl + "/tournaments", tournamentDTO, TournamentDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la création du tournoi", ex);
        }
    }

    /**
     * Met à jour les informations d'un tournoi existant.
     * 
     * @param id            L'ID du tournoi à mettre à jour.
     * @param tournamentDTO L'objet TournamentDTO contenant les nouvelles
     *                      informations du tournoi.
     * @return true si la mise à jour a réussi, false sinon.
     * @throws RuntimeException si une erreur survient lors de la mise à jour du
     *                          tournoi.
     */
    public boolean updateTournament(Integer id, TournamentDTO tournamentDTO) {
        try {
            restTemplate.put(serviceRest2BaseUrl + "/tournaments/" + id, tournamentDTO);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la mise à jour du tournoi", ex);
        }
    }

    /**
     * Supprime un tournoi en fonction de son identifiant.
     * 
     * @param id L'ID du tournoi à supprimer.
     * @return true si la suppression a réussi, false sinon.
     * @throws RuntimeException si une erreur survient lors de la suppression du
     *                          tournoi.
     */
    public boolean deleteTournament(Integer id) {
        try {
            restTemplate.delete(serviceRest2BaseUrl + "/tournaments/" + id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la suppression du tournoi", ex);
        }
    }

    // -------------------------- MATCHS --------------------------

    /**
     * Récupère la liste de tous les matchs.
     * 
     * @return Une liste contenant tous les matchs disponibles.
     * @throws RuntimeException si une erreur survient lors de la récupération des
     *                          matchs.
     */
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

    /**
     * Récupère un match spécifique par son identifiant.
     * 
     * @param id L'ID du match à récupérer.
     * @return L'objet MatchDTO représentant le match avec l'ID donné.
     * @throws RuntimeException si une erreur survient lors de la récupération du
     *                          match.
     */
    public MatchDTO getMatchById(Integer id) {
        try {
            return restTemplate.getForObject(serviceRest2BaseUrl + "/matches/" + id, MatchDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération du match", ex);
        }
    }

    /**
     * Crée un nouveau match avec les informations spécifiées.
     * 
     * @param matchDTO L'objet MatchDTO contenant les informations du match à créer.
     * @return L'objet MatchDTO représentant le match créé.
     * @throws RuntimeException si une erreur survient lors de la création du match.
     */
    public MatchDTO createMatch(MatchDTO matchDTO) {
        try {
            return restTemplate.postForObject(serviceRest2BaseUrl + "/matches", matchDTO, MatchDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la création du match", ex);
        }
    }

    /**
     * Met à jour les informations d'un match existant.
     * 
     * @param id       L'ID du match à mettre à jour.
     * @param matchDTO L'objet MatchDTO contenant les nouvelles informations du
     *                 match.
     * @return true si la mise à jour a réussi, false sinon.
     * @throws RuntimeException si une erreur survient lors de la mise à jour du
     *                          match.
     */
    public boolean updateMatch(Integer id, MatchDTO matchDTO) {
        try {
            restTemplate.put(serviceRest2BaseUrl + "/matches/" + id, matchDTO);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la mise à jour du match", ex);
        }
    }

    /**
     * Supprime un match en fonction de son identifiant.
     * 
     * @param id L'ID du match à supprimer.
     * @return true si la suppression a réussi, false sinon.
     * @throws RuntimeException si une erreur survient lors de la suppression du
     *                          match.
     */
    public boolean deleteMatch(Integer id) {
        try {
            restTemplate.delete(serviceRest2BaseUrl + "/matches/" + id);
            return true;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la suppression du match", ex);
        }
    }

    // -------------------------- JEUX --------------------------

    /**
     * Récupère la liste de tous les jeux.
     * 
     * @return Une liste contenant tous les jeux disponibles.
     * @throws RuntimeException si une erreur survient lors de la récupération des
     *                          jeux.
     */
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

    /**
     * Récupère un jeu spécifique par son identifiant.
     * 
     * @param id L'ID du jeu à récupérer.
     * @return L'objet GameDTO représentant le jeu avec l'ID donné.
     * @throws RuntimeException si une erreur survient lors de la récupération du
     *                          jeu.
     */
    public GameDTO getGameById(Integer id) {
        try {
            return restTemplate.getForObject(serviceRest2BaseUrl + "/games/" + id, GameDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Erreur lors de la récupération du jeu", ex);
        }
    }

}
