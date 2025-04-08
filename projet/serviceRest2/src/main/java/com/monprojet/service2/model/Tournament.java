package com.monprojet.service2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "t_tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_tournaments")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "date", nullable = false)
    private String date;

    // Identifiant de l'administrateur (fk_admin)
    @Column(name = "fk_admin", nullable = false)
    private Integer adminId;
    
    // Identifiant du jeu (fk_game)
    @Column(name = "fk_game", nullable = false)
    private Integer gameId;

    public Tournament() { }

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
    
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    
    public Integer getAdminId() {
        return adminId;
    }
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
    
    public Integer getGameId() {
        return gameId;
    }
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
}
