package com.monprojet.service2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_tournaments")
    private Integer id;
    
    @Column(name = "name", length = 45, nullable = false, unique = true)
    private String name;
    
    @Column(name = "date", length = 45, nullable = false)
    private String date;
    
    @ManyToOne
    @JoinColumn(name = "fk_game", nullable = false)
    private Game game;
    
    @Column(name = "fk_admin", nullable = false)
    private Integer adminId;
    
    @Column(name = "status", nullable = false)
    private Boolean status;
    
    // Constructeurs
    public Tournament() {
    }
    
    public Tournament(Integer id, String name, String date, Game game, Integer adminId, Boolean status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.game = game;
        this.adminId = adminId;
        this.status = status;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
} 