package com.monprojet.service2.dto;

public class MatchDTO {
    private Integer id;
    private Integer tournamentId;
    private Integer team1Id;
    private Integer team2Id;
    private String score1;
    private String score2;
    private String date; // Format ISO (par exemple "2025-04-08")

    public MatchDTO() { }

    public MatchDTO(Integer id, Integer tournamentId, Integer team1Id, Integer team2Id,
                    String score1, String score2, String date) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.score1 = score1;
        this.score2 = score2;
        this.date = date;
    }

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
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
