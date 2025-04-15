package com.monprojet.service1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.monprojet.service1.dto.TeamDTO;
import com.monprojet.service1.dto.TournamentDTO;
import com.monprojet.service1.dto.UserDTO;
import com.monprojet.service1.models.Team;
import com.monprojet.service1.models.User;
import com.monprojet.service1.repositories.TeamRepository;
import com.monprojet.service1.repositories.UserRepository;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    // Récupérer toutes les équipes
    public List<TeamDTO> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teams.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Récupérer une équipe par son ID
    public TeamDTO getTeamById(Integer id) {
        Optional<Team> teamOpt = teamRepository.findById(id);
        return teamOpt.map(this::convertToDTO).orElse(null);
    }

    // Récupérer toutes les équipes par l'ID du capitaine
    public List<TeamDTO> getTeamsByCaptain(Integer captainId) {
        // On suppose que le repository possède une méthode findByCaptainId
        List<Team> teams = teamRepository.findByCaptainId(captainId);
        return teams.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Récupérer toutes les équipes par ID de tournoi (si nécessaire)
    public List<TeamDTO> getTeamsByTournament(Integer tournamentId) {
        // On suppose que le repository possède une méthode findByTournamentId
        List<Team> teams = teamRepository.findByTournamentId(tournamentId);
        return teams.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TeamDTO createTeam(String name, Integer captainId, Integer tournamentId) {
        // Récupérer le capitaine à partir de l'ID
        Optional<User> captainOpt = userRepository.findById(captainId);
        if (!captainOpt.isPresent()) {
            // Gérer le cas où le capitaine n'est pas trouvé
            return null;
        }
        User captain = captainOpt.get();

        // Récupérer le tournoi à partir de l'ID
        Optional<TournamentDTO> tournamentOpt = tournamentRepository.findById(tournamentId); // Tu peux aussi utiliser
                                                                                             // un service pour
                                                                                             // récupérer le tournoi
        if (!tournamentOpt.isPresent()) {
            // Gérer le cas où le tournoi n'est pas trouvé
            return null;
        }
        TournamentDTO tournament = tournamentOpt.get();

        // Créer une nouvelle équipe avec le tournoi et le capitaine associés
        Team team = new Team();
        team.setName(name);
        team.setCaptain(captain);
        team.setTournamentId(tournamentId); // Associe l'ID du tournoi à l'équipe

        // Sauvegarder l'équipe dans la base de données
        Team savedTeam = teamRepository.save(team);

        // Convertir l'entité Team en DTO et renvoyer la réponse
        return convertToDTO(savedTeam);
    }

    // Mettre à jour une équipe existante
    public TeamDTO updateTeam(Integer id, String name, Integer captainId, Integer tournamentId) {
        Optional<Team> teamOpt = teamRepository.findById(id);
        if (!teamOpt.isPresent()) {
            return null;
        }
        Team team = teamOpt.get();
        team.setName(name);
        team.setTournamentId(tournamentId);

        // Mise à jour du capitaine si disponible
        Optional<User> captainOpt = userRepository.findById(captainId);
        if (captainOpt.isPresent()) {
            team.setCaptain(captainOpt.get());
        }
        Team updatedTeam = teamRepository.save(team);
        return convertToDTO(updatedTeam);
    }

    // Supprimer une équipe par ID
    public boolean deleteTeam(Integer id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Méthode utilitaire pour convertir l'entité Team en TeamDTO
    private TeamDTO convertToDTO(Team team) {
        User captain = team.getCaptain();
        UserDTO captainDTO = new UserDTO(captain.getId(), captain.getName(), captain.getEmail());

        // Récupérer le tournoi associé à l'équipe
        TournamentDTO tournamentDTO = new TournamentDTO(team.getTournamentId(), "Nom du tournoi", "Jeu du tournoi");

        return new TeamDTO(team.getId(), team.getName(), captainDTO, tournamentDTO);
    }

}
