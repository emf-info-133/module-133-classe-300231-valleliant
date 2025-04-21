package com.monprojet.service1.services;

import com.monprojet.service1.models.Team;
import com.monprojet.service1.models.TeamUser;
import com.monprojet.service1.models.User;
import com.monprojet.service1.repositories.TeamRepository;
import com.monprojet.service1.repositories.TeamUserRepository;
import com.monprojet.service1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service qui gère les relations entre les utilisateurs et les équipes dans un tournoi.
 * Permet d'ajouter ou de supprimer des utilisateurs dans les équipes et de vérifier des conditions liées à leur appartenance.
 */
@Service
public class TeamUserService {

    @Autowired
    private TeamUserRepository teamUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    /**
     * Ajoute un utilisateur à une équipe dans un tournoi.
     * 
     * @param userId L'identifiant de l'utilisateur à ajouter.
     * @param teamId L'identifiant de l'équipe dans laquelle l'utilisateur doit être ajouté.
     * @return Un objet {@link TeamUser} représentant la relation entre l'utilisateur et l'équipe.
     * @throws RuntimeException Si l'utilisateur ou l'équipe n'est pas trouvé, ou si l'utilisateur est déjà capitaine d'une équipe dans ce tournoi, ou si l'utilisateur fait déjà partie de l'équipe.
     */
    public TeamUser addUserToTeam(Integer userId, Integer teamId) {
        // Récupération de l'utilisateur et de l'équipe
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Team> teamOpt = teamRepository.findById(teamId);

        if (userOpt.isEmpty() || teamOpt.isEmpty()) {
            throw new RuntimeException("Utilisateur ou équipe introuvable");
        }

        Team team = teamOpt.get();
        Integer tournamentId = team.getTournamentId();

        // Vérification si l'utilisateur est déjà capitaine d'une équipe dans ce tournoi
        Optional<Team> captainTeamOpt = teamRepository.findByCaptainIdAndTournamentId(userId, tournamentId);
        if (captainTeamOpt.isPresent()) {
            throw new RuntimeException("L'utilisateur est déjà capitaine d'une équipe dans ce tournoi.");
        }

        // Vérification si l'utilisateur est déjà membre de l'équipe
        Optional<TeamUser> existingMember = teamUserRepository.findByUserIdAndTeamId(userId, teamId);
        if (existingMember.isPresent()) {
            throw new RuntimeException("L'utilisateur fait déjà partie de cette équipe.");
        }

        // Ajout de l'utilisateur à l'équipe
        TeamUser teamUser = new TeamUser();
        teamUser.setUser(userOpt.get());
        teamUser.setTeam(team);

        return teamUserRepository.save(teamUser);
    }

    /**
     * Supprime un utilisateur d'une équipe.
     * 
     * @param userId L'identifiant de l'utilisateur à supprimer.
     * @param teamId L'identifiant de l'équipe dont l'utilisateur doit être supprimé.
     * @return true si l'utilisateur a été supprimé avec succès, sinon false si l'utilisateur ne faisait pas partie de l'équipe.
     */
    public boolean deleteUserFromTeam(Integer userId, Integer teamId) {
        Optional<TeamUser> teamUserOpt = teamUserRepository.findByUserIdAndTeamId(userId, teamId);

        if (teamUserOpt.isPresent()) {
            teamUserRepository.delete(teamUserOpt.get());
            return true;
        }

        return false; // L'utilisateur ne faisait pas partie de cette équipe
    }

    /**
     * Vérifie si un utilisateur est déjà capitaine ou membre d'une équipe dans un tournoi.
     * 
     * @param userId L'identifiant de l'utilisateur à vérifier.
     * @param tournamentId L'identifiant du tournoi à vérifier.
     * @return true si l'utilisateur est déjà capitaine ou membre d'une équipe dans le tournoi, sinon false.
     */
    public boolean isUserAlreadyInTeamForTournament(Integer userId, Integer tournamentId) {
        boolean isCaptain = teamRepository.existsByCaptainIdAndTournamentId(userId, tournamentId);
        boolean isMember = teamUserRepository.existsByUser_IdAndTeam_TournamentId(userId, tournamentId);
        return isCaptain || isMember;
    }
}
