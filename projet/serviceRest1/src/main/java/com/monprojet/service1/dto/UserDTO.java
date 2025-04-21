package com.monprojet.service1.dto;

public class UserDTO {
    private Integer id;
    private String name;
    private String email;
    private boolean isAdmin; // Champ ajouté pour vérifier le rôle administrateur

    public UserDTO() {
    }

    public UserDTO(Integer id, String name, String email, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin; // Initialisation du champ isAdmin
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

    public boolean isAdmin() {
        return isAdmin;
    } // Getter pour isAdmin

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    } // Setter pour isAdmin
}
