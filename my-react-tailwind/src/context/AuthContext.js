import React, { createContext, useState, useContext, useEffect } from 'react';
import { login as apiLogin, logout as apiLogout, register as apiRegister } from '../services/api';
import apiClient from '../services/api'; // Importer pour vérifier la session au chargement

// Exporter explicitement le contexte pour qu'il puisse être importé dans useAuth.js
export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true); // Pour gérer le chargement initial

  // Vérifier la session au chargement de l'application
  useEffect(() => {
    const checkSession = async () => {
      try {
        // Essayer d'accéder à une route protégée (ex: /api/users) pour voir si la session est valide
        // Ou mieux, avoir un endpoint dédié /auth/me
        // Pour l'instant, on suppose qu'une requête réussie à /api/users signifie qu'on est connecté
        // Attention: Cela nécessite une modification du backend ou une approche différente
        // si /api/users n'est pas approprié ou si aucun utilisateur n'existe.
        // Alternative: stocker un token/flag dans localStorage après connexion réussie
        // Ici, faute de mieux, on tente /api/users

        // Note: Pour une vraie app, il faudrait un endpoint /auth/status ou /me
        // Simulé ici : si un cookie de session existe, on essaie de récupérer l'user
        // On simule la récupération de l'utilisateur pour le démo
        // Il faudrait que /auth/login retourne l'UserDTO
        console.warn("Simulation de checkSession: manque endpoint /auth/me. Doit être implémenté côté backend.");
        // TODO: Remplacer par un vrai appel à /auth/me ou un endpoint similaire
        // const response = await apiClient.get('/auth/me'); // Endpoint idéal
        // setUser(response.data);

        // Temporairement, on ne fait rien ici, l'état initial est 'non connecté'
      } catch (error) {
        console.log("Aucune session active trouvée.");
        setUser(null);
      } finally {
        setLoading(false);
      }
    };
    checkSession();
  }, []);

  const login = async (email, password) => {
    try {
      setLoading(true);
      const response = await apiLogin(email, password);
      // IMPORTANT: Le backend DOIT retourner les infos de l'utilisateur (UserDTO) ici
      // Pour l'instant, l'API retourne juste "Connexion réussie!"
      // Il faut modifier le backend pour retourner l'UserDTO ou faire un appel GET /api/users/me
      // Simulation:
      console.warn("Simulation login: le backend doit retourner UserDTO sur /auth/login");
      // TODO: Remplacer par les vraies données de response.data quand le backend sera mis à jour
      const simulatedUser = {
          id: Date.now(), // ID Fictif
          name: 'Utilisateur Connecté', // Nom Fictif
          email: email,
          // Ajoutez d'autres champs si nécessaire depuis UserDTO
      };
      setUser(simulatedUser);
      return response; // Retourne la réponse originale (peut contenir des messages)
    } catch (error) {
      setUser(null);
      throw error; // Propage l'erreur pour la gérer dans le composant
    } finally {
      setLoading(false);
    }
  };

  const register = async (name, email, password) => {
    try {
      setLoading(true);
      const response = await apiRegister(name, email, password);
      // L'API d'enregistrement retourne déjà UserDTO, on peut l'utiliser
      // setUser(response.data); // On pourrait connecter l'utilisateur directement après l'inscription
      console.log("Inscription réussie, veuillez vous connecter.");
      return response;
    } catch (error) {
       throw error;
    } finally {
      setLoading(false);
    }
  };

  const logout = async () => {
    try {
      setLoading(true);
      await apiLogout();
      setUser(null);
    } catch (error) {
      console.error("Erreur lors de la déconnexion:", error);
      // Gérer l'erreur si nécessaire (ex: forcer la déconnexion côté client)
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  const isAdmin = () => {
    // L'API Gateway détermine l'admin par email.
    // Assurez-vous que l'objet 'user' contient l'email.
    return user?.email === 'admin@admin.com';
  };

  const value = {
    user,
    loading,
    login,
    logout,
    register,
    isAuthenticated: !!user,
    isAdmin: isAdmin(),
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children} {/* Affiche les enfants seulement quand le chargement initial est terminé */}
    </AuthContext.Provider>
  );
};

// Hook useAuth exporté ici (peut aussi être placé dans un fichier séparé)
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};