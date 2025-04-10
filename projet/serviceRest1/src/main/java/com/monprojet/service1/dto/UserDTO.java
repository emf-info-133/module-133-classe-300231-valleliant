package com.monprojet.service1.dto;

public class UserDTO {
    private Integer id;
    private String name;
    private String email;

    // Constructeur vide pour la sérialisation/désérialisation
    public UserDTO() {
    }

    // Constructeur complet
    public UserDTO(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
