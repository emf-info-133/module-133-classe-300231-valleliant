package com.monprojet.service1.dto;

public class TournamentDTO {
    private Integer id;
    private String name;
    private String gameName; // Le nom du jeu auquel le tournoi est lié

    // Constructeur vide
    public TournamentDTO() {
    }

    // Constructeur complet
    public TournamentDTO(Integer id, String name, String gameName) {
        this.id = id;
        this.name = name;
        this.gameName = gameName;
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

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
