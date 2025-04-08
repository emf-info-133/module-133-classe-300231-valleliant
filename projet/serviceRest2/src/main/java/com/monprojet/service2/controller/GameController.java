package com.monprojet.service2.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.monprojet.service2.dto.GameDTO;
import com.monprojet.service2.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/games")
@Tag(name = "Jeux", description = "API pour gérer les jeux")
public class GameController {

    private final GameService gameService;
    
    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
    
    @GetMapping
    @Operation(summary = "Récupérer tous les jeux", description = "Renvoie la liste de tous les jeux")
    public ResponseEntity<List<GameDTO>> getAllGames() {
        List<GameDTO> games = gameService.getAllGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un jeu par ID", description = "Renvoie un jeu spécifique par son ID")
    public ResponseEntity<GameDTO> getGameById(@PathVariable Integer id) {
        GameDTO game = gameService.getGameById(id);
        if (game != null) {
            return new ResponseEntity<>(game, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @PostMapping
    @Operation(summary = "Créer un nouveau jeu", description = "Crée un nouveau jeu et renvoie ses détails")
    public ResponseEntity<GameDTO> createGame(@RequestBody GameDTO gameDTO) {
        GameDTO createdGame = gameService.createGame(gameDTO);
        return new ResponseEntity<>(createdGame, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un jeu", description = "Met à jour un jeu existant par son ID")
    public ResponseEntity<GameDTO> updateGame(@PathVariable Integer id, @RequestBody GameDTO gameDTO) {
        GameDTO updatedGame = gameService.updateGame(id, gameDTO);
        if (updatedGame != null) {
            return new ResponseEntity<>(updatedGame, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un jeu", description = "Supprime un jeu par son ID")
    public ResponseEntity<Void> deleteGame(@PathVariable Integer id) {
        boolean deleted = gameService.deleteGame(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                       : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
