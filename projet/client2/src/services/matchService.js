import axios from 'axios';
import { API_URL } from '../config';

// URL de base pour les matchs via la passerelle
const GATEWAY_API = `${API_URL}/gateway`;

// Récupérer tous les matchs
export const getAllMatches = async () => {
  try {
    const response = await axios.get(`${GATEWAY_API}/matches`, {
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
    const response = await axios.get(`${GATEWAY_API}/matches/${id}`, {
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
    const response = await axios.get(`${GATEWAY_API}/tournaments/${tournamentId}/matches`, {
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
    const response = await axios.get(`${GATEWAY_API}/teams/${teamId}/matches`, {
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
    const response = await axios.post(`${GATEWAY_API}/matches`, matchData, {
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
    const response = await axios.put(`${GATEWAY_API}/matches/${id}`, matchData, {
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
    const response = await axios.delete(`${GATEWAY_API}/matches/${id}`, {
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
export const updateMatchScore = async (id, score1, score2) => {
  try {
    const response = await axios.put(`${GATEWAY_API}/matches/${id}/score`, {
      score1: score1,
      score2: score2
    }, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la mise à jour du score du match ${id}:`, error);
    throw error;
  }
}; 