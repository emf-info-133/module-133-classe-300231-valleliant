package com.monprojet.apigateway.dto;

public class TournamentWithAdminDTO {
    private Integer tournamentId;
    private String tournamentName;
    private String tournamentDate;
    private Integer adminId;
    private String adminName;
    private String adminEmail;

    public TournamentWithAdminDTO() { }

    public TournamentWithAdminDTO(Integer tournamentId, String tournamentName, String tournamentDate,
                                  Integer adminId, String adminName, String adminEmail) {
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.tournamentDate = tournamentDate;
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
    }

    // Getters et Setters
    public Integer getTournamentId() { return tournamentId; }
    public void setTournamentId(Integer tournamentId) { this.tournamentId = tournamentId; }

    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }

    public String getTournamentDate() { return tournamentDate; }
    public void setTournamentDate(String tournamentDate) { this.tournamentDate = tournamentDate; }

    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }

    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }

    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }
}
