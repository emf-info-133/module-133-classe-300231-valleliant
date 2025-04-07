import axios from 'axios';
import { API_URL } from '../config';

// URL de base pour les matchs
const MATCH_API = `${API_URL}/service1/matches`;

// Récupérer tous les matchs
export const getAllMatches = async () => {
  try {
    const response = await axios.get(MATCH_API, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des matchs:', error);
    throw error;
  }
};

// Récupérer un match par son ID
export const getMatchById = async (id) => {
  try {
    const response = await axios.get(`${MATCH_API}/${id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du match avec l'ID ${id}:`, error);
    throw error;
  }
};

// Récupérer les matchs d'un tournoi
export const getMatchesByTournament = async (tournamentId) => {
  try {
    const response = await axios.get(`${MATCH_API}/tournament/${tournamentId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération des matchs du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

// Récupérer les matchs d'une équipe
export const getMatchesByTeam = async (teamId) => {
  try {
    const response = await axios.get(`${MATCH_API}/team/${teamId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération des matchs de l'équipe ${teamId}:`, error);
    throw error;
  }
};

// Créer un nouveau match
export const createMatch = async (matchData) => {
  try {
    const response = await axios.post(MATCH_API, matchData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la création du match:', error);
    throw error;
  }
};

// Mettre à jour un match
export const updateMatch = async (id, matchData) => {
  try {
    const response = await axios.put(`${MATCH_API}/${id}`, matchData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la mise à jour du match avec l'ID ${id}:`, error);
    throw error;
  }
};

// Supprimer un match
export const deleteMatch = async (id) => {
  try {
    const response = await axios.delete(`${MATCH_API}/${id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la suppression du match avec l'ID ${id}:`, error);
    throw error;
  }
};

// Mettre à jour le score d'un match
export const updateMatchScore = async (id, scoreA, scoreB) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 500));
  
  const matchId = parseInt(id, 10);
  const index = mockMatches.findIndex(m => m.id === matchId);
  
  if (index === -1) {
    throw new Error('Match non trouvé');
  }
  
  // Mettre à jour le score
  mockMatches[index] = {
    ...mockMatches[index],
    scoreA: scoreA,
    scoreB: scoreB,
    status: 'completed'
  };
  
  return { ...mockMatches[index] };
}; 