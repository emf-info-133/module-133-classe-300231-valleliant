package com.monprojet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.monprojet.dto.TournamentWithAdminDTO;


@Service
public class TournamentIntegrationService {

    private final RestTemplate restTemplate;

    // URL des microservices, définies dans l'application.properties de l'API
    // Gateway
    @Value("${serviceRest2.tournament.url}")
    private String tournamentServiceUrl;

    @Value("${serviceRest1.user.url}")
    private String userServiceUrl;

    public TournamentIntegrationService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public TournamentWithAdminDTO getTournamentWithAdmin(Integer tournamentId) {
        // Appel au service REST 2 pour récupérer les détails du tournoi
        String tournamentUrl = tournamentServiceUrl + "/" + tournamentId;
        TournamentDTO tournament = restTemplate.getForObject(tournamentUrl, TournamentDTO.class);

        // On suppose que l'objet TournamentDTO contient un champ adminId
        Integer adminId = tournament.getAdminId();

        // Appel au service REST 1 pour récupérer les détails de l'administrateur
        String userUrl = userServiceUrl + "/" + adminId;
        UserDTO admin = restTemplate.getForObject(userUrl, UserDTO.class);

        // Assemblage des données dans le DTO composite
        TournamentWithAdminDTO dto = new TournamentWithAdminDTO();
        dto.setTournamentId(tournament.getId());
        dto.setTournamentName(tournament.getName());
        dto.setTournamentDate(tournament.getDate());
        dto.setAdminId(admin.getId());
        dto.setAdminName(admin.getName());
        dto.setAdminEmail(admin.getEmail());

        return dto;
    }
}
