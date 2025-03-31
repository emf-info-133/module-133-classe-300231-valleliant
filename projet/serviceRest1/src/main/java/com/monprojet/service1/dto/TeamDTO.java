package com.monprojet.service1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    private Integer id;
    private String name;
    private UserDTO captain;
    private TournamentDTO tournament;
} 