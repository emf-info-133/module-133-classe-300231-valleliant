package com.monprojet.apigateway.dto;

public class TeamDTO {
    private Integer id;
    private String name;
    private UserDTO captain; // Le capitaine de l'équipe
    private TournamentDTO tournament; // Le tournoi auquel l'équipe est associée

    // Constructeurs
    public TeamDTO() { }

    public TeamDTO(Integer id, String name, UserDTO captain, TournamentDTO tournament) {
        this.id = id;
        this.name = name;
        this.captain = captain;
        this.tournament = tournament;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UserDTO getCaptain() { return captain; }
    public void setCaptain(UserDTO captain) { this.captain = captain; }

    public TournamentDTO getTournament() { return tournament; }
    public void setTournament(TournamentDTO tournament) { this.tournament = tournament; }
}
