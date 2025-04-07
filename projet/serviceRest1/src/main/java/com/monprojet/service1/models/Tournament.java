package com.monprojet.service1.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_tournaments")
public class Tournament {

    @Id
    @Column(name = "pk_tournaments")
    private Integer id;
    
    // Constructeurs
    public Tournament() {
    }
    
    public Tournament(Integer id) {
        this.id = id;
    }
    
    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
} 