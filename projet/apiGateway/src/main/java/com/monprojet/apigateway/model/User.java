package com.monprojet.apigateway.model;
 
import jakarta.persistence.*;
import lombok.*;
 
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 45)
    private String name;
    @Column(nullable = false, length = 45, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean isAdmin;
}