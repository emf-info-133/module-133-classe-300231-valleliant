package com.example.ex7.dto;

public class SkieurDTO {
    private Integer id;
    private String name;
    private Integer position;
    private String paysNom; // Nom du pays pour simplifier, vous pouvez inclure d'autres propriétés si nécessaire

    // Constructeurs, getters et setters
    public SkieurDTO() {}

    public SkieurDTO(Integer id, String name, Integer position, String paysNom) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.paysNom = paysNom;
    }

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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getPaysNom() {
        return paysNom;
    }

    public void setPaysNom(String paysNom) {
        this.paysNom = paysNom;
    }

    
}