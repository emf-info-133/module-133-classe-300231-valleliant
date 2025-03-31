package com.monprojet.service2.dto;

public class TeamDTO {
    private Integer id;
    private TournamentDTO tournament;

    // Constructeurs
    public TeamDTO() {
    }

    public TeamDTO(Integer id, TournamentDTO tournament) {
        this.id = id;
        this.tournament = tournament;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TournamentDTO getTournament() {
        return tournament;
    }

    public void setTournament(TournamentDTO tournament) {
        this.tournament = tournament;
    }
} 