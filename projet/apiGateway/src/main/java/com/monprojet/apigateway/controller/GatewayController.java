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

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final GatewayService gatewayService;

    @Autowired
    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    /**
     * Vérifie si un utilisateur est connecté en vérifiant la session HTTP.
     * 
     * @param request La requête HTTP contenant les informations de session.
     * @return true si l'utilisateur est connecté, false sinon.
     */
    private boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }

    /**
     * Vérifie si l'utilisateur connecté est un administrateur.
     * 
     * @param request La requête HTTP contenant les informations de session.
     * @return true si l'utilisateur est un administrateur, false sinon.
     */
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false; // Pas de session active
        }

        String identifiant = (String) session.getAttribute("user");
        if (identifiant == null || identifiant.isEmpty()) {
            return false; // Pas d'identifiant dans la session
        }

        try {
            // Récupérer l'utilisateur à partir de son identifiant
            UserDTO currentUser = gatewayService.findUserByEmailOrName(identifiant);
            if (currentUser == null) {
                return false; // Utilisateur non trouvé
            }

            // Vérifier si l'utilisateur est administrateur
            return currentUser.isAdmin();
        } catch (Exception e) {
            // Log l'exception et renvoyer false en cas d'erreur
            return false;
        }
    }

    // --- USERS ---

    /**
     * Récupère tous les utilisateurs.
     * 
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant la liste des utilisateurs ou un code 401
     *         si l'utilisateur n'est pas connecté.
     */
    @GetMapping("/users")
    @Operation(summary = "Obtenir tous les utilisateurs")
    public ResponseEntity<List<UserDTO>> getUsers(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        List<UserDTO> users = gatewayService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    /**
     * Récupère un utilisateur par son identifiant.
     * 
     * @param id      L'identifiant de l'utilisateur à récupérer.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant l'utilisateur ou un code 404 si
     *         l'utilisateur n'est pas trouvé.
     */
    @GetMapping("/users/{id}")
    @Operation(summary = "Obtenir un utilisateur par ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        UserDTO user = gatewayService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * Met à jour un utilisateur par son identifiant.
     * L'utilisateur peut mettre à jour ses propres informations ou un
     * administrateur peut mettre à jour n'importe quel utilisateur.
     * 
     * @param id      L'identifiant de l'utilisateur à mettre à jour.
     * @param userDTO Les nouvelles informations de l'utilisateur.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP indiquant si la mise à jour a été effectuée ou non.
     */
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

    /**
     * Récupère toutes les équipes, ou filtre les équipes par tournoi si un
     * `tournamentId` est fourni.
     * 
     * @param tournamentId L'identifiant du tournoi pour filtrer les équipes, peut
     *                     être null.
     * @param request      La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant la liste des équipes ou un code 401 si
     *         l'utilisateur n'est pas connecté.
     */
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

    /**
     * Récupère une équipe par son identifiant.
     * 
     * @param id      L'identifiant de l'équipe à récupérer.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant l'équipe ou un code 404 si l'équipe n'est
     *         pas trouvée.
     */
    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build();
        }
        TeamDTO team = gatewayService.getTeamById(id);
        return team != null ? ResponseEntity.ok(team) : ResponseEntity.notFound().build();
    }

    /**
     * Crée une nouvelle équipe. L'utilisateur connecté est défini comme capitaine
     * si ce n'est pas déjà spécifié.
     * 
     * @param teamDTO Les données de l'équipe à créer.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant l'équipe créée, avec un code 201 si la
     *         création a réussi.
     */
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

    /**
     * Met à jour une équipe par son identifiant. Seul l'administrateur ou le
     * capitaine de l'équipe peut effectuer la mise à jour.
     * 
     * @param id      L'identifiant de l'équipe à mettre à jour.
     * @param teamDTO Les nouvelles données de l'équipe.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP indiquant si la mise à jour a été effectuée ou non.
     */
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

    /**
     * Supprime une équipe par son identifiant. Seul un administrateur ou le
     * capitaine de l'équipe peut effectuer la suppression.
     * 
     * @param id      L'identifiant de l'équipe à supprimer.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP indiquant si la suppression a été effectuée ou non.
     */
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

    // ---------------- TEAMS-USER ----------------

    /**
     * Permet à un utilisateur de rejoindre une équipe.
     * 
     * @param request     L'objet contenant les informations nécessaires pour
     *                    rejoindre l'équipe, notamment l'identifiant de l'équipe.
     * @param httpRequest La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant l'objet `TeamUserDTO` qui associe
     *         l'utilisateur à l'équipe, ou un code 401 si l'utilisateur n'est pas
     *         connecté ou si une erreur se produit.
     */
    @PostMapping("/teams/join")
    public ResponseEntity<TeamUserDTO> joinTeam(@RequestBody TeamUserDTO request, HttpServletRequest httpRequest) {
        if (!isUserLoggedIn(httpRequest)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }

        HttpSession session = httpRequest.getSession(false);
        String identifiant = (String) session.getAttribute("user");

        UserDTO currentUser = gatewayService.findUserByEmailOrName(identifiant);
        if (currentUser == null) {
            return ResponseEntity.status(401).build(); // Utilisateur non trouvé
        }

        // Associer l'utilisateur à l'équipe demandée
        request.setUserId(currentUser.getId());

        // Appeler le service pour associer l'utilisateur à l'équipe
        TeamUserDTO result = gatewayService.joinTeam(request);
        return new ResponseEntity<>(result, HttpStatus.CREATED); // Retourner l'objet de l'association avec un code 201
    }

    @DeleteMapping("/teams/leave")
    @Operation(summary = "Supprimer un utilisateur d'une équipe")
    public ResponseEntity<Void> deleteUserFromTeam(@RequestBody TeamUserDTO teamUserDTO, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }

        // Récupérer l'équipe actuelle
        TeamDTO existingTeam = gatewayService.getTeamById(teamUserDTO.getTeamId());
        if (existingTeam == null) {
            return ResponseEntity.notFound().build(); // L’équipe n’existe pas
        }

        HttpSession session = request.getSession(false);
        String identifiant = (String) session.getAttribute("user");

        UserDTO currentUser = gatewayService.findUserByEmailOrName(identifiant);
        if (currentUser == null) {
            return ResponseEntity.status(401).build(); // Aucun utilisateur trouvé
        }

        // Vérifier si l’utilisateur est administrateur, capitaine de l’équipe ou le
        // même utilisateur qui quitte
        if (!(isAdmin(request) || existingTeam.getCaptain().equals(currentUser.getId())
                || currentUser.getId().equals(teamUserDTO.getUserId()))) {
            return ResponseEntity.status(403).build(); // Non autorisé
        }

        // Utiliser le service pour quitter l'équipe
        boolean left = gatewayService.leaveTeam(teamUserDTO);
        return left ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ---------------- TOURNAMENTS ----------------

    /**
     * Récupère tous les tournois.
     * 
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant la liste des tournois ou un code 401 si
     *         l'utilisateur n'est pas connecté.
     */
    @GetMapping("/tournaments")
    public ResponseEntity<List<TournamentDTO>> getAllTournaments(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }
        List<TournamentDTO> tournaments = gatewayService.getAllTournaments();
        return tournaments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tournaments);
    }

    /**
     * Récupère un tournoi par son ID.
     * 
     * @param id      L'identifiant du tournoi.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant le tournoi trouvé ou un code 401 si
     *         l'utilisateur n'est pas connecté.
     */
    @GetMapping("/tournaments/{id}")
    public ResponseEntity<TournamentDTO> getTournamentById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }
        TournamentDTO tournament = gatewayService.getTournamentById(id);
        return tournament != null ? ResponseEntity.ok(tournament) : ResponseEntity.notFound().build();
    }

    /**
     * Crée un nouveau tournoi.
     * 
     * @param dto     L'objet TournamentDTO contenant les informations du tournoi à
     *                créer.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant le tournoi créé avec un code 201 si la
     *         création a réussi, ou un code 401 si l'utilisateur n'est pas connecté
     *         ou un code 403 si l'utilisateur n'est pas administrateur.
     */
    @PostMapping("/tournaments")
    public ResponseEntity<TournamentDTO> createTournament(@RequestBody TournamentDTO dto, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build(); // Utilisateur non administrateur
        }

        // Récupérer l'utilisateur connecté
        HttpSession session = request.getSession(false);
        String identifiant = (String) session.getAttribute("user");

        UserDTO currentUser = gatewayService.findUserByEmailOrName(identifiant);

        // Définir l'utilisateur connecté comme capitaine si ce n'est pas déjà le cas
        if (dto.getAdminId() == null) {
            dto.setAdminId(currentUser.getId());
        }

        TournamentDTO created = gatewayService.createTournament(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED); // Tournoi créé
    }

    /**
     * Met à jour un tournoi existant par son ID.
     * 
     * @param id      L'identifiant du tournoi à mettre à jour.
     * @param dto     L'objet TournamentDTO contenant les informations mises à jour
     *                du tournoi.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP avec code 204 si la mise à jour a réussi, ou un code
     *         401 si l'utilisateur n'est pas connecté, ou un code 403 si
     *         l'utilisateur n'est pas administrateur.
     */
    @PutMapping("/tournaments/{id}")
    public ResponseEntity<Void> updateTournament(@PathVariable Integer id, @RequestBody TournamentDTO dto,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build(); // Utilisateur non administrateur
        }
        boolean updated = gatewayService.updateTournament(id, dto);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); // Mise à jour réussie
                                                                                                 // ou tournoi non
                                                                                                 // trouvé
    }

    /**
     * Supprime un tournoi par son ID.
     * 
     * @param id      L'identifiant du tournoi à supprimer.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP avec code 204 si la suppression a réussi, ou un code
     *         401 si l'utilisateur n'est pas connecté, ou un code 403 si
     *         l'utilisateur n'est pas administrateur.
     */
    @DeleteMapping("/tournaments/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build(); // Utilisateur non administrateur
        }
        boolean deleted = gatewayService.deleteTournament(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); // Suppression réussie
                                                                                                 // ou tournoi non
                                                                                                 // trouvé
    }

    // ---------------- MATCHES ----------------

    /**
     * Récupère tous les matchs.
     * 
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant la liste des matchs ou un code 401 si
     *         l'utilisateur n'est pas connecté.
     */
    @GetMapping("/matches")
    public ResponseEntity<List<MatchDTO>> getAllMatches(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }
        List<MatchDTO> matches = gatewayService.getAllMatches();
        return matches.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(matches);
    }

    /**
     * Récupère un match par son ID.
     * 
     * @param id      L'identifiant du match.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant le match trouvé ou un code 401 si
     *         l'utilisateur n'est pas connecté.
     */
    @GetMapping("/matches/{id}")
    public ResponseEntity<MatchDTO> getMatchById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }
        MatchDTO match = gatewayService.getMatchById(id);
        return match != null ? ResponseEntity.ok(match) : ResponseEntity.notFound().build();
    }

    /**
     * Crée un nouveau match.
     * 
     * @param dto     L'objet MatchDTO contenant les informations du match à créer.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant le match créé avec un code 201 si la
     *         création a réussi, ou un code 401 si l'utilisateur n'est pas connecté
     *         ou un code 403 si l'utilisateur n'est pas administrateur.
     */
    @PostMapping("/matches")
    public ResponseEntity<MatchDTO> createMatch(@RequestBody MatchDTO dto, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build(); // Utilisateur non administrateur
        }
        MatchDTO created = gatewayService.createMatch(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED); // Match créé
    }

    /**
     * Met à jour un match existant par son ID.
     * 
     * @param id      L'identifiant du match à mettre à jour.
     * @param dto     L'objet MatchDTO contenant les informations mises à jour du
     *                match.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP avec code 204 si la mise à jour a réussi, ou un code
     *         401 si l'utilisateur n'est pas connecté, ou un code 403 si
     *         l'utilisateur n'est pas administrateur.
     */
    @PutMapping("/matches/{id}")
    public ResponseEntity<Void> updateMatch(@PathVariable Integer id, @RequestBody MatchDTO dto,
            HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build(); // Utilisateur non administrateur
        }
        boolean updated = gatewayService.updateMatch(id, dto);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); // Mise à jour réussie
                                                                                                 // ou match non trouvé
    }

    /**
     * Supprime un match par son ID.
     * 
     * @param id      L'identifiant du match à supprimer.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP avec code 204 si la suppression a réussi, ou un code
     *         401 si l'utilisateur n'est pas connecté, ou un code 403 si
     *         l'utilisateur n'est pas administrateur.
     */
    @DeleteMapping("/matches/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build(); // Utilisateur non administrateur
        }
        boolean deleted = gatewayService.deleteMatch(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build(); // Suppression réussie
                                                                                                 // ou match non trouvé
    }

    // ---------------- GAMES ----------------

    /**
     * Récupère tous les jeux disponibles.
     * 
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant la liste des jeux ou un code 401 si
     *         l'utilisateur n'est pas connecté.
     */
    @GetMapping("/games")
    public ResponseEntity<List<GameDTO>> getGames(HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }
        List<GameDTO> games = gatewayService.getAllGames();
        return games.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(games); // Jeux récupérés ou
                                                                                                // aucun jeu disponible
    }

    /**
     * Récupère un jeu par son ID.
     * 
     * @param id      L'identifiant du jeu.
     * @param request La requête HTTP contenant les informations de session.
     * @return Une réponse HTTP contenant le jeu trouvé ou un code 401 si
     *         l'utilisateur n'est pas connecté.
     */
    @GetMapping("/games/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable Integer id, HttpServletRequest request) {
        if (!isUserLoggedIn(request)) {
            return ResponseEntity.status(401).build(); // Utilisateur non connecté
        }
        GameDTO game = gatewayService.getGameById(id);
        return game != null ? ResponseEntity.ok(game) : ResponseEntity.notFound().build(); // Jeu trouvé ou non trouvé
    }

}
