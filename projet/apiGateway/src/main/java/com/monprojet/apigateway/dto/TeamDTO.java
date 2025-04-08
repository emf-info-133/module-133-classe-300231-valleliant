package com.monprojet.apigateway.dto;

public class TeamDTO {
    private Integer id;
    private String name;
    private UserDTO captain; // Par exemple, le capitaine de l'équipe

    public TeamDTO() { }

    public TeamDTO(Integer id, String name, UserDTO captain) {
        this.id = id;
        this.name = name;
        this.captain = captain;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UserDTO getCaptain() { return captain; }
    public void setCaptain(UserDTO captain) { this.captain = captain; }
}
