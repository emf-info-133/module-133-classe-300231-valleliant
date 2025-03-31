package com.monprojet.service1.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_teams_has_t_ranking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pfk_teams_ranking")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "t_teams_pk_teams", nullable = false)
    private Team team;
    
    @ManyToOne
    @JoinColumn(name = "t_ranking_pk_ranking", nullable = false)
    private Ranking ranking;
} 