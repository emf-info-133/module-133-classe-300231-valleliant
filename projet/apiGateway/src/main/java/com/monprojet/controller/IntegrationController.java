package com.monprojet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.monprojet.dto.TournamentWithAdminDTO;
import com.monprojet.service.TournamentIntegrationService;

@RestController
@RequestMapping("/api/integration")
public class IntegrationController {

    private final TournamentIntegrationService integrationService;

    @Autowired
    public IntegrationController(TournamentIntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @GetMapping("/tournaments/{id}")
    public ResponseEntity<TournamentWithAdminDTO> getTournamentWithAdmin(@PathVariable Integer id) {
        TournamentWithAdminDTO dto = integrationService.getTournamentWithAdmin(id);
        return ResponseEntity.ok(dto);
    }
}
