package com.monprojet.service1.models;

import jakarta.persistence.*;

@Entity
@Table(name = "t_teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_teams")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    // Référence au capitaine, clé étrangère vers t_user
    @ManyToOne
    @JoinColumn(name = "fk_capitaine", nullable = false)
    private User captain;

    // Optionnel : Si l'équipe est associée à un tournoi
    @Column(name = "fk_tournaments", nullable = false)
    private Integer tournamentId;

    // Constructeurs, getters et setters
    public Team() {
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
    public User getCaptain() {
        return captain;
    }
    public void setCaptain(User captain) {
        this.captain = captain;
    }
    public Integer getTournamentId() {
        return tournamentId;
    }
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }
}
