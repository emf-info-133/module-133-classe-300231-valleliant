package com.monprojet.apigateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.monprojet.apigateway.dto.TournamentDTO;
import com.monprojet.apigateway.dto.UserDTO;
import com.monprojet.apigateway.dto.TournamentWithAdminDTO;

@Service
public class TournamentIntegrationService {

    private final RestTemplate restTemplate;
    
    // URL des microservices définies dans application.properties
    @Value("${serviceRest2.tournament.url}")
    private String tournamentServiceUrl;
    
    @Value("${serviceRest1.user.url}")
    private String userServiceUrl;
    
    public TournamentIntegrationService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    
    public TournamentWithAdminDTO getTournamentWithAdmin(Integer tournamentId) {
        // Récupérer le tournoi depuis serviceRest2
        TournamentDTO tournament = restTemplate.getForObject(tournamentServiceUrl + "/" + tournamentId, TournamentDTO.class);
        if (tournament == null) {
            return null;
        }
        // Récupérer l'administrateur grâce à adminId dans le tournoi via serviceRest1
        UserDTO admin = restTemplate.getForObject(userServiceUrl + "/" + tournament.getAdminId(), UserDTO.class);
        
        return new TournamentWithAdminDTO(
            tournament.getId(),
            tournament.getName(),
            tournament.getDate(),
            tournament.getAdminId(),
            (admin != null ? admin.getName() : null),
            (admin != null ? admin.getEmail() : null)
        );
    }
}
