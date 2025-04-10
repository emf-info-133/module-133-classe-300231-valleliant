package com.monprojet.service1.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_user")
    private Integer id;
    
    @Column(nullable = false, length = 45)
    private String name;
    
    @Column(nullable = false, length = 45, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    // Pour simplifier, ajout d'un booléen pour indiquer si l'utilisateur est admin (optionnel)
    @Column(nullable = false)
    private boolean isAdmin;
}
