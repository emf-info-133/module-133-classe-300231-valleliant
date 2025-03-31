package com.monprojet.service2.dto;

public class AdminDTO {
    private Integer id;
    private String name;
    private String email;
    // On n'inclut pas le mot de passe pour des raisons de sécurité

    // Constructeurs
    public AdminDTO() {
    }

    public AdminDTO(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
} 