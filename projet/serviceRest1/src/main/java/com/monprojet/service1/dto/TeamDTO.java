package com.monprojet.service1.dto;

public class TeamDTO {
    private Integer id;
    private String name;
    private Integer captain;        // Le capitaine de l'équipe
    private Integer tournament;     // Le tournoi auquel l'équipe est associée

    // Constructeur vide
    public TeamDTO() {
    }

    // Constructeur complet
    public TeamDTO(Integer id, String name, Integer captain, Integer tournament) {
        this.id = id;
        this.name = name;
        this.captain = captain;
        this.tournament = tournament;
    }

    // Getters et setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getCaptain() {
        return captain;
    }
    public void setCaptain(Integer captain) {
        this.captain = captain;
    }
    public Integer getTournament() {
        return tournament;
    }
    public void setTournament(Integer tournament) {
        this.tournament = tournament;
    }
}
