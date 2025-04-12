// Nouvelle version de GatewayService utilisant WebClient
package com.monprojet.apigateway.service;

import com.monprojet.apigateway.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class GatewayService {

    private final WebClient webClient;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${serviceRest1.base.url}")
    private String serviceRest1BaseUrl;

    @Value("${serviceRest2.base.url}")
    private String serviceRest2BaseUrl;

    public GatewayService(WebClient.Builder webClientBuilder, BCryptPasswordEncoder passwordEncoder) {
        this.webClient = webClientBuilder.build();
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<List<UserDTO>> getAllUsers() {
        return webClient.get()
                .uri(serviceRest1BaseUrl + "/users")
                .retrieve()
                .bodyToFlux(UserDTO.class)
                .collectList();
    }

    public Mono<UserDTO> getUserById(Integer id) {
        return webClient.get()
                .uri(serviceRest1BaseUrl + "/users/" + id)
                .retrieve()
                .bodyToMono(UserDTO.class);
    }

    public Mono<UserDTO> createUser(UserDTO userDTO, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);

        Map<String, String> payload = new HashMap<>();
        payload.put("name", userDTO.getName());
        payload.put("email", userDTO.getEmail());
        payload.put("password", hashedPassword);

        return webClient.post()
                .uri(serviceRest1BaseUrl + "/users")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(UserDTO.class);
    }

    public Mono<List<TeamDTO>> getAllTeams() {
        return webClient.get()
                .uri(serviceRest1BaseUrl + "/teams")
                .retrieve()
                .bodyToFlux(TeamDTO.class)
                .collectList();
    }

    public Mono<TeamDTO> getTeamById(Integer id) {
        return webClient.get()
                .uri(serviceRest1BaseUrl + "/teams/" + id)
                .retrieve()
                .bodyToMono(TeamDTO.class);
    }

    public Mono<List<TournamentDTO>> getAllTournaments() {
        return webClient.get()
                .uri(serviceRest2BaseUrl + "/tournaments")
                .retrieve()
                .bodyToFlux(TournamentDTO.class)
                .collectList();
    }

    public Mono<TournamentDTO> getTournamentById(Integer id) {
        return webClient.get()
                .uri(serviceRest2BaseUrl + "/tournaments/" + id)
                .retrieve()
                .bodyToMono(TournamentDTO.class);
    }

    public Mono<List<GameDTO>> getAllGames() {
        return webClient.get()
                .uri(serviceRest2BaseUrl + "/games")
                .retrieve()
                .bodyToFlux(GameDTO.class)
                .collectList();
    }

    public Mono<List<MatchDTO>> getAllMatches() {
        return webClient.get()
                .uri(serviceRest2BaseUrl + "/matches")
                .retrieve()
                .bodyToFlux(MatchDTO.class)
                .collectList();
    }

    public Mono<List<MatchDTO>> getMatchesByTeam(Integer teamId) {
        return webClient.get()
                .uri(serviceRest2BaseUrl + "/matches/team/" + teamId)
                .retrieve()
                .bodyToFlux(MatchDTO.class)
                .collectList();
    }

    public Mono<TournamentWithAdminDTO> getTournamentWithAdmin(Integer tournamentId) {
        Mono<TournamentDTO> tournamentMono = getTournamentById(tournamentId);

        return tournamentMono.flatMap(tournament ->
                getUserById(tournament.getAdminId()).map(admin ->
                        new TournamentWithAdminDTO(
                                tournament.getId(),
                                tournament.getName(),
                                tournament.getDate(),
                                tournament.getAdminId(),
                                admin != null ? admin.getName() : null,
                                admin != null ? admin.getEmail() : null
                        )));
    }
}
