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
@Table(name = "t_matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @JoinColumn(name = "fk_teams2", nullable = false)
    private Team team2;
} 