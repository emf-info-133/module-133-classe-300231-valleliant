package com.monprojet.service1.security;

import org.springframework.stereotype.Service;
import com.monprojet.service1.dto.UserDTO;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemorySessionService {

    private final Map<String, UserDTO> sessions = new ConcurrentHashMap<>();

    // Crée une session pour un utilisateur
    public void createSession(String token, UserDTO user) {
        sessions.put(token, user);  // Associer l'utilisateur à son token
    }

    // Récupère la session pour un token donné
    public UserDTO getSession(String token) {
        return sessions.get(token);  // Récupérer l'utilisateur associé au token
    }

    // Supprime la session pour un token donné
    public void removeSession(String token) {
        sessions.remove(token);  // Supprimer la session pour ce token
    }
}

