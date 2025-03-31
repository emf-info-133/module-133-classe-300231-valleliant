package com.monprojet.service2.dto;

import java.util.Date;

public class MatchDTO {
    private Integer id;
    private TournamentDTO tournament;
    private TeamDTO team1;
    private TeamDTO team2;
    private String score1;
    private String score2;
    private Date date;

    // Constructeurs
    public MatchDTO() {
    }

    public MatchDTO(Integer id, TournamentDTO tournament, TeamDTO team1, TeamDTO team2, String score1,
            String score2, Date date) {
        this.id = id;
        this.tournament = tournament;
        this.team1 = team1;
        this.team2 = team2;
        this.score1 = score1;
        this.score2 = score2;
        this.date = date;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TournamentDTO getTournament() {
        return tournament;
    }

    public void setTournament(TournamentDTO tournament) {
        this.tournament = tournament;
    }

    public TeamDTO getTeam1() {
        return team1;
    }

    public void setTeam1(TeamDTO team1) {
        this.team1 = team1;
    }

    public TeamDTO getTeam2() {
        return team2;
    }

    public void setTeam2(TeamDTO team2) {
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