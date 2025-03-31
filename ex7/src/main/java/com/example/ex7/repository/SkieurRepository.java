package com.example.ex7.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.ex7.model.Skieur;

// This will be AUTO IMPLEMENTED by Spring into a Bean called SkieurRepository
// CRUD refers Create, Read, Update, Delete

public interface SkieurRepository extends CrudRepository<Skieur, Integer> {

}