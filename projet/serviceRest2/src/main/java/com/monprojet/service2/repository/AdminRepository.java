package com.monprojet.service2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.monprojet.service2.model.Admin;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {
    // Méthodes personnalisées si nécessaire
} 