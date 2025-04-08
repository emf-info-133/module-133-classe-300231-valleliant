package com.monprojet.apigateway.dto;

public class TournamentDTO {
    private Integer id;
    private String name;
    private String date;
    private Integer adminId;  // Référence à l'administrateur, obtenu depuis serviceRest2

    public TournamentDTO() { }

    public TournamentDTO(Integer id, String name, String date, Integer adminId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.adminId = adminId;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }
}
