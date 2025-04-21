package com.monprojet.service1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.monprojet.service1.dto.TeamDTO;
import com.monprojet.service1.dto.UserDTO;
import com.monprojet.service1.models.Team;
import com.monprojet.service1.models.User;
import com.monprojet.service1.repositories.TeamRepository;
import com.monprojet.service1.repositories.UserRepository;

/**
 * Service qui gère la logique métier liée aux équipes dans le cadre d'un
 * tournoi.
 * Permet de récupérer, créer, mettre à jour, et supprimer des équipes.
 */
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamUserService teamUserService;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository, TeamUserService teamUserService) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teamUserService = teamUserService;
    }

    /**
     * Récupère toutes les équipes.
     * 
     * @return Une liste de {@link TeamDTO} représentant toutes les équipes.
     */
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Récupère une équipe par son identifiant.
     * 
     * @param id L'identifiant de l'équipe à récupérer.
     * @return Le {@link TeamDTO} de l'équipe trouvée ou null si l'équipe n'existe
     *         pas.
     */
    public TeamDTO getTeamById(Integer id) {
        return teamRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    /**
     * Récupère toutes les équipes dirigées par un capitaine spécifié.
     * 
     * @param captainId L'identifiant du capitaine.
     * @return Une liste de {@link TeamDTO} représentant les équipes dirigées par le
     *         capitaine.
     */
    public List<TeamDTO> getTeamsByCaptain(Integer captainId) {
        return teamRepository.findByCaptainId(captainId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Récupère toutes les équipes d'un tournoi spécifié.
     * 
     * @param tournamentId L'identifiant du tournoi.
     * @return Une liste de {@link TeamDTO} représentant les équipes du tournoi.
     */
    public List<TeamDTO> getTeamsByTournament(Integer tournamentId) {
        return teamRepository.findByTournamentId(tournamentId).stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crée une nouvelle équipe dans un tournoi.
     * 
     * @param name         Le nom de l'équipe.
     * @param captainId    L'identifiant du capitaine de l'équipe.
     * @param tournamentId L'identifiant du tournoi auquel appartient l'équipe.
     * @return Le {@link TeamDTO} de l'équipe créée.
     * @throws IllegalArgumentException Si le capitaine n'existe pas, si le nom est
     *                                  déjà pris ou si le capitaine est déjà membre
     *                                  d'une autre équipe dans ce tournoi.
     */
    public TeamDTO createTeam(String name, Integer captainId, Integer tournamentId) {

        // Vérification de l'existence du capitaine
        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new IllegalArgumentException("Capitaine non trouvé."));

        // Vérification du nom de l'équipe
        if (teamRepository.existsByNameAndTournamentId(name, tournamentId)) {
            throw new IllegalArgumentException("Une équipe avec ce nom existe déjà dans ce tournoi.");
        }

        // Vérification que l'utilisateur n'est pas déjà capitaine ou membre d'une autre
        // équipe
        if (teamRepository.existsByCaptainIdAndTournamentId(captainId, tournamentId)) {
            throw new IllegalArgumentException("Cet utilisateur est déjà capitaine d'une équipe dans ce tournoi.");
        }

        if (teamUserService.isUserAlreadyInTeamForTournament(captainId, tournamentId)) {
            throw new IllegalArgumentException("Cet utilisateur fait déjà partie d'une équipe dans ce tournoi.");
        }

        // Création de l'équipe
        Team team = new Team();
        team.setName(name);
        team.setCaptain(captain);
        team.setTournamentId(tournamentId);

        return convertToDTO(teamRepository.save(team));
    }

    /**
     * Met à jour une équipe existante.
     * 
     * @param id           L'identifiant de l'équipe à mettre à jour.
     * @param name         Le nouveau nom de l'équipe.
     * @param captainId    L'identifiant du nouveau capitaine (peut être nul).
     * @param tournamentId L'identifiant du tournoi auquel appartient l'équipe.
     * @return Le {@link TeamDTO} de l'équipe mise à jour.
     * @throws IllegalArgumentException Si l'équipe n'existe pas, si le nom est déjà
     *                                  pris, ou si le capitaine existe déjà dans
     *                                  une autre équipe du tournoi.
     */
    public TeamDTO updateTeam(Integer id, String name, Integer captainId, Integer tournamentId) {

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Équipe non trouvée"));

        // Vérification de doublon du nom
        if (teamRepository.existsByNameAndTournamentIdAndIdNot(name, tournamentId, id)) {
            throw new IllegalArgumentException("Une autre équipe avec ce nom existe déjà dans ce tournoi.");
        }

        // Mise à jour du nom et tournoi
        team.setName(name);
        team.setTournamentId(tournamentId);

        // Si le capitaine change, vérification
        if (captainId != null && (team.getCaptain() == null || !captainId.equals(team.getCaptain().getId()))) {

            // Vérification du capitaine
            User captain = userRepository.findById(captainId)
                    .orElseThrow(() -> new IllegalArgumentException("Capitaine non trouvé."));

            if (teamRepository.existsByCaptainIdAndTournamentId(captainId, tournamentId)) {
                throw new IllegalArgumentException(
                        "Cet utilisateur est déjà capitaine d'une autre équipe dans ce tournoi.");
            }

            if (teamUserService.isUserAlreadyInTeamForTournament(captainId, tournamentId)) {
                throw new IllegalArgumentException("Cet utilisateur fait déjà partie d'une équipe dans ce tournoi.");
            }

            team.setCaptain(captain);
        }

        return convertToDTO(teamRepository.save(team));
    }

    /**
     * Supprime une équipe.
     * 
     * @param id L'identifiant de l'équipe à supprimer.
     * @return true si l'équipe a été supprimée avec succès, sinon false si l'équipe
     *         n'existe pas.
     */
    public boolean deleteTeam(Integer id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Convertit une entité {@link Team} en un DTO {@link TeamDTO}.
     * 
     * @param team L'équipe à convertir.
     * @return Le DTO correspondant à l'équipe.
     */
    private TeamDTO convertToDTO(Team team) {
        return new TeamDTO(team.getId(), team.getName(), team.getCaptain().getId(), team.getTournamentId());
    }
}
