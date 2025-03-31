package com.monprojet.service1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    private Integer id;
    private TournamentDTO tournament;
    private TeamDTO team1;
    private TeamDTO team2;
} 