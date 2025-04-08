package com.monprojet.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.monprojet.apigateway.dto.TournamentWithAdminDTO;
import com.monprojet.apigateway.service.TournamentIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/integration")
@Tag(name = "Intégration", description = "API d'orchestration entre les services REST")
public class IntegrationController {

    private final TournamentIntegrationService integrationService;

    @Autowired
    public IntegrationController(TournamentIntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @GetMapping("/tournaments/{id}")
    @Operation(summary = "Récupérer un tournoi avec administrateur", description = "Renvoie le tournoi ainsi que les informations de l'administrateur associé")
    public ResponseEntity<TournamentWithAdminDTO> getTournamentWithAdmin(@PathVariable Integer id) {
        TournamentWithAdminDTO dto = integrationService.getTournamentWithAdmin(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}
