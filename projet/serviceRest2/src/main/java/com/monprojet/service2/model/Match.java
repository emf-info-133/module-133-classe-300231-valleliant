package com.monprojet.service2.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "t_matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_matches")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "fk_tournaments", nullable = false)
    private Tournament tournament;
    
    @ManyToOne
    @JoinColumn(name = "fk_team1", nullable = false)
    private Team team1;
    
    @ManyToOne
    @JoinColumn(name = "fk_team2", nullable = false)
    private Team team2;
    
    @Column(name = "score1", length = 45)
    private String score1;
    
    @Column(name = "score2", length = 45)
    private String score2;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date date;

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public String getScore1() {
        return score1;
    }

    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public String getScore2() {
        return score2;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
} 