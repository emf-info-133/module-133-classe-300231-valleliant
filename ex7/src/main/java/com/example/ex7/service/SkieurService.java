package com.example.ex7.service;
import com.example.ex7.dto.SkieurDTO;
import com.example.ex7.model.Pays;
import com.example.ex7.model.Skieur;
import com.example.ex7.repository.PaysRepository;
import com.example.ex7.repository.SkieurRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkieurService {

    private final SkieurRepository skieurRepository;
    private final PaysRepository paysRepository;

    @Autowired
    public SkieurService(SkieurRepository skieurRepository, PaysRepository paysRepository) {
        this.skieurRepository = skieurRepository;
        this.paysRepository = paysRepository;
    }

    public Iterable<SkieurDTO> findAllSkieurs() {
        Iterable<Skieur> skieurs = skieurRepository.findAll();
        List<SkieurDTO> skieurDTOs = new ArrayList<>();
        for (Skieur skieur : skieurs) {
            SkieurDTO skieurDTO = new SkieurDTO(
                    skieur.getId(),
                    skieur.getName(),
                    skieur.getPosition(),
                    skieur.getPays() != null ? skieur.getPays().getNom() : null);
            skieurDTOs.add(skieurDTO);
        }
        return skieurDTOs;
    }

    @Transactional
    public String addNewSkieur(String name, Integer position, Integer paysId) {
        Pays pays = paysRepository.findById(paysId).orElse(null);
        if (pays == null) {
            return "Pays not found";
        }
        Skieur newSkieur = new Skieur();
        newSkieur.setName(name);
        newSkieur.setPosition(position);
        newSkieur.setPays(pays);
        skieurRepository.save(newSkieur);
        return "Saved";
    }
}