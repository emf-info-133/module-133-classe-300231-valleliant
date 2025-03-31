package com.monprojet.service2.dto;

public class TournamentDTO {
    private Integer id;
    private String name;
    private String date;
    private GameDTO game;
    private AdminDTO admin;
    private Boolean status;

    // Constructeurs
    public TournamentDTO() {
    }

    public TournamentDTO(Integer id, String name, String date, GameDTO game, AdminDTO admin, Boolean status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.game = game;
        this.admin = admin;
        this.status = status;
    }

    // Getters et Setters
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public AdminDTO getAdmin() {
        return admin;
    }

    public void setAdmin(AdminDTO admin) {
        this.admin = admin;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
} 