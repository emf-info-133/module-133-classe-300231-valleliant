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
@Table(name = "tr_team_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pfk_team_user")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "fk_teams", nullable = false)
    private Team team;
} 