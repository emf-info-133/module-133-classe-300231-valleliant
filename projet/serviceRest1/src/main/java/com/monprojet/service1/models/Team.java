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
@Table(name = "t_teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_teams")
    private Integer id;
    
    @Column(name = "name", length = 45, nullable = false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "fk_capitaine", nullable = false)
    private User captain;
    
    @ManyToOne
    @JoinColumn(name = "fk_tournaments", nullable = false)
    private Tournament tournament;
} 