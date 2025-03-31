package com.monprojet.service1.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    
    @Column(name = "name", length = 45, nullable = false)
    private String name;
    
    @Column(name = "email", length = 45, nullable = false)
    private String email;
    
    @Column(name = "password", length = 255, nullable = false)
    private String password;
} 