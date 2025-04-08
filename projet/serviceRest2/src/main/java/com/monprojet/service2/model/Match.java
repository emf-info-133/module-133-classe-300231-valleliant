package com.monprojet.service2.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "t_matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_matches")
    private Integer id;

    @Column(name = "fk_tournaments", nullable = false)
    private Integer tournamentId;

    @Column(name = "fk_team1", nullable = false)
    private Integer team1Id;

    @Column(name = "fk_team2", nullable = false)
    private Integer team2Id;

    @Column(name = "score1")
    private String score1;

    @Column(name = "score2")
    private String score2;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    public Match() { }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getTournamentId() {
        return tournamentId;
    }
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }
    public Integer getTeam1Id() {
        return team1Id;
    }
    public void setTeam1Id(Integer team1Id) {
        this.team1Id = team1Id;
    }
    public Integer getTeam2Id() {
        return team2Id;
    }
    public void setTeam2Id(Integer team2Id) {
        this.team2Id = team2Id;
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
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
