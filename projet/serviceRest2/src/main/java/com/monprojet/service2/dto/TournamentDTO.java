package com.monprojet.service2.dto;

public class TournamentDTO {
    private Integer id;
    private String name;
    private String date;
    private Integer adminId; // Identifiant de l'administrateur
    private Integer gameId;  // Identifiant du jeu associé

    public TournamentDTO() { }

    public TournamentDTO(Integer id, String name, String date, Integer adminId, Integer gameId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.adminId = adminId;
        this.gameId = gameId;
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
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public Integer getAdminId() {
        return adminId;
    }
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
    public Integer getGameId() {
        return gameId;
    }
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
}
