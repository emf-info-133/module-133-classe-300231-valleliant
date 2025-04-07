package com.monprojet.service1.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_teams")
    private Integer id;
    
    @Column(name = "name", length = 45, nullable = false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "fk_capitaine", nullable = false)
    private User captain;
    
    @ManyToOne
    @JoinColumn(name = "fk_tournaments", nullable = false)
    private Tournament tournament;
    
    // Constructeurs
    public Team() {
    }
    
    public Team(Integer id, String name, User captain, Tournament tournament) {
        this.id = id;
        this.name = name;
        this.captain = captain;
        this.tournament = tournament;
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

    public User getCaptain() {
        return captain;
    }

    public void setCaptain(User captain) {
        this.captain = captain;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
} 