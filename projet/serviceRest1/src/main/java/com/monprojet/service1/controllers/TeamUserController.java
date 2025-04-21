package com.monprojet.service1.controllers;

import com.monprojet.service1.dto.TeamUserDTO;
import com.monprojet.service1.models.TeamUser;
import com.monprojet.service1.services.TeamUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur qui gère les opérations liées à l'adhésion et à la suppression des utilisateurs dans les équipes.
 * Il permet aux utilisateurs de rejoindre ou quitter une équipe.
 */
@RestController
@RequestMapping("api/team-users")
public class TeamUserController {

    private final TeamUserService teamUserService;

    @Autowired
    public TeamUserController(TeamUserService teamUserService) {
        this.teamUserService = teamUserService;
    }

    /**
     * Permet à un utilisateur de rejoindre une équipe.
     * 
     * @param request Un objet DTO contenant l'ID de l'utilisateur et l'ID de l'équipe.
     * @return Un objet TeamUser représentant la relation entre l'utilisateur et l'équipe.
     */
    @PostMapping("/join")
    public TeamUser joinTeam(@RequestBody TeamUserDTO request) {
        return teamUserService.addUserToTeam(request.getUserId(), request.getTeamId());
    }

    /**
     * Permet à un utilisateur de quitter une équipe.
     * 
     * @param request Un objet DTO contenant l'ID de l'utilisateur et l'ID de l'équipe.
     * @return Une réponse HTTP indiquant si l'utilisateur a été supprimé de l'équipe ou non.
     */
    @DeleteMapping("/leave")
    public ResponseEntity<String> leaveTeam(@RequestBody TeamUserDTO request) {
        boolean removed = teamUserService.deleteUserFromTeam(request.getUserId(), request.getTeamId());

        if (removed) {
            return ResponseEntity.noContent().build(); // 204 No Content si l'utilisateur a bien quitté l'équipe
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found si l'utilisateur ou l'équipe n'existe pas
        }
    }
}
