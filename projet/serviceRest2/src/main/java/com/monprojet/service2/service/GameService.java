package com.monprojet.service2.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.monprojet.service2.dto.GameDTO;
import com.monprojet.service2.model.Game;
import com.monprojet.service2.repository.GameRepository;

@Service
public class GameService {

    private final GameRepository gameRepository;
    
    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
    
    public List<GameDTO> getAllGames() {
        List<Game> games = gameRepository.findAll();
        return games.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
    }
    
    public GameDTO getGameById(Integer id) {
        Optional<Game> gameOpt = gameRepository.findById(id);
        return gameOpt.map(this::convertToDTO)
                      .orElse(null);
    }
    
    public GameDTO createGame(GameDTO gameDTO) {
        Game game = new Game();
        game.setName(gameDTO.getName());
        game.setType(gameDTO.getType());
        Game saved = gameRepository.save(game);
        return convertToDTO(saved);
    }
    
    public GameDTO updateGame(Integer id, GameDTO gameDTO) {
        Optional<Game> gameOpt = gameRepository.findById(id);
        if (!gameOpt.isPresent()) return null;
        Game game = gameOpt.get();
        game.setName(gameDTO.getName());
        game.setType(gameDTO.getType());
        Game updated = gameRepository.save(game);
        return convertToDTO(updated);
    }
    
    public boolean deleteGame(Integer id) {
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private GameDTO convertToDTO(Game game) {
        return new GameDTO(game.getId(), game.getName(), game.getType());
    }
}
