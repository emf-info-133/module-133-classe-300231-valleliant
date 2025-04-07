package com.monprojet.service2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.monprojet.service2.dto.TournamentDTO;
import com.monprojet.service2.service.TournamentService;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping
    public ResponseEntity<List<TournamentDTO>> getAllTournaments() {
        List<TournamentDTO> tournaments = tournamentService.getAllTournaments();
        return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentDTO> getTournamentById(@PathVariable Integer id) {
        TournamentDTO tournament = tournamentService.getTournamentById(id);
        if (tournament != null) {
            return new ResponseEntity<>(tournament, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<TournamentDTO> addTournament(@RequestBody TournamentRequest tournamentRequest) {
        TournamentDTO tournament = tournamentService.addTournament(
            tournamentRequest.getName(),
            tournamentRequest.getDate(),
            tournamentRequest.getGameId(),
            tournamentRequest.getAdminId(),
            tournamentRequest.getStatus()
        );
        if (tournament != null) {
            return new ResponseEntity<>(tournament, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentDTO> updateTournament(
            @PathVariable Integer id,
            @RequestBody TournamentRequest tournamentRequest) {
        TournamentDTO tournament = tournamentService.updateTournament(
            id,
            tournamentRequest.getName(),
            tournamentRequest.getDate(),
            tournamentRequest.getGameId(),
            tournamentRequest.getAdminId(),
            tournamentRequest.getStatus()
        );
        if (tournament != null) {
            return new ResponseEntity<>(tournament, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Integer id) {
        boolean deleted = tournamentService.deleteTournament(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Classe pour les requêtes de tournoi
    public static class TournamentRequest {
        private String name;
        private String date;
        private Integer gameId;
        private Integer adminId;
        private Boolean status;
        
        // Getters et Setters
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDate() {
            return date;
        }
        
        public void setDate(String date) {
            this.date = date;
        }
        
        public Integer getGameId() {
            return gameId;
        }
        
        public void setGameId(Integer gameId) {
            this.gameId = gameId;
        }
        
        public Integer getAdminId() {
            return adminId;
        }
        
        public void setAdminId(Integer adminId) {
            this.adminId = adminId;
        }
        
        public Boolean getStatus() {
            return status;
        }
        
        public void setStatus(Boolean status) {
            this.status = status;
        }
    }
} 