package com.monprojet.service2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monprojet.service2.dto.GameDTO;
import com.monprojet.service2.model.Game;
import com.monprojet.service2.repository.GameRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    
    private final GameRepository gameRepository;
    
    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
    
    // Récupérer tous les jeux
    public List<GameDTO> getAllGames() {
        List<GameDTO> gameDTOs = new ArrayList<>();
        gameRepository.findAll().forEach(game -> {
            gameDTOs.add(convertToDTO(game));
        });
        return gameDTOs;
    }
    
    // Récupérer un jeu par ID
    public GameDTO getGameById(Integer id) {
        Optional<Game> game = gameRepository.findById(id);
        return game.map(this::convertToDTO).orElse(null);
    }
    
    // Ajouter un nouveau jeu
    @Transactional
    public GameDTO addGame(String name, String type) {
        Game game = new Game();
        game.setName(name);
        game.setType(type);
        
        Game savedGame = gameRepository.save(game);
        return convertToDTO(savedGame);
    }
    
    // Mettre à jour un jeu existant
    @Transactional
    public GameDTO updateGame(Integer id, String name, String type) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            if (name != null) game.setName(name);
            if (type != null) game.setType(type);
            
            Game updatedGame = gameRepository.save(game);
            return convertToDTO(updatedGame);
        }
        
        return null;
    }
    
    // Supprimer un jeu
    @Transactional
    public boolean deleteGame(Integer id) {
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Convertir un Game en GameDTO
    private GameDTO convertToDTO(Game game) {
        return new GameDTO(
            game.getId(),
            game.getName(),
            game.getType()
        );
    }
} 