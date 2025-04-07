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
@Table(name = "tr_team_user")
public class TeamUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pfk_team_user")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "fk_teams", nullable = false)
    private Team team;
    
    // Constructeurs
    public TeamUser() {
    }
    
    public TeamUser(Integer id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
    }
    
    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
} 