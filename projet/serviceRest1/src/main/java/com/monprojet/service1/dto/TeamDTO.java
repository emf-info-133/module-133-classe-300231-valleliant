package com.monprojet.service1.dto;

public class TeamDTO {
    private Integer id;
    private String name;
    private UserDTO captain;       // Le capitaine de l'équipe
    // On peut aussi inclure d'autres informations, comme le tournoi auquel elle participe

    // Constructeur vide
    public TeamDTO() {
    }

    // Constructeur complet
    public TeamDTO(Integer id, String name, UserDTO captain) {
        this.id = id;
        this.name = name;
        this.captain = captain;
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
    public UserDTO getCaptain() {
        return captain;
    }
    public void setCaptain(UserDTO captain) {
        this.captain = captain;
    }
}
