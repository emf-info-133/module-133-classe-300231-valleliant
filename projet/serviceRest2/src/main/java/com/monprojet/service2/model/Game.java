package com.monprojet.service2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_games")
    private Integer id;
    
    @Column(name = "name", length = 45, nullable = false, unique = true)
    private String name;
    
    @Column(name = "type", length = 45, nullable = false)
    private String type;
    
    // Constructeurs
    public Game() {
    }
    
    public Game(Integer id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
} 