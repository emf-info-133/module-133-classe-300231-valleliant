import axios from 'axios';
import { API_URL } from '../config';

// URL de base via la passerelle
const GATEWAY_API = `${API_URL}/gateway`;

// Récupérer tous les classements
export const getAllRankings = async () => {
  try {
    const response = await axios.get(`${GATEWAY_API}/rankings`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des classements:', error);
    throw error;
  }
};

// Récupérer le classement d'un tournoi
export const getRankingByTournament = async (tournamentId) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/tournaments/${tournamentId}/rankings`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du classement du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

// Mettre à jour le classement d'un tournoi
export const updateRanking = async (tournamentId, rankingData) => {
  try {
    const response = await axios.put(`${GATEWAY_API}/tournaments/${tournamentId}/rankings`, rankingData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la mise à jour du classement du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

// Calcule automatiquement le classement en fonction des résultats des matchs
export const calculateRanking = async (tournamentId) => {
  try {
    const response = await axios.post(`${GATEWAY_API}/tournaments/${tournamentId}/rankings/calculate`, {}, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors du calcul du classement du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

// Mettre à jour le classement d'une équipe
export const updateTeamRanking = async (tournamentId, teamId, updateData) => {
  try {
    const response = await axios.put(`${GATEWAY_API}/tournaments/${tournamentId}/rankings/teams/${teamId}`, updateData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la mise à jour du classement de l'équipe ${teamId}:`, error);
    throw error;
  }
};

// Créer un nouveau classement pour un tournoi
export const createRanking = async (tournamentId) => {
  try {
    const response = await axios.post(`${GATEWAY_API}/tournaments/${tournamentId}/rankings`, {}, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la création du classement pour le tournoi ${tournamentId}:`, error);
    throw error;
  }
};

// Supprimer un classement
export const deleteRanking = async (tournamentId) => {
  try {
    const response = await axios.delete(`${GATEWAY_API}/tournaments/${tournamentId}/rankings`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la suppression du classement du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

// Mettre à jour un classement après un match
export const updateRankingAfterMatch = async (tournamentId, matchId) => {
  try {
    const response = await axios.post(`${GATEWAY_API}/tournaments/${tournamentId}/rankings/updateAfterMatch/${matchId}`, {}, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la mise à jour du classement après le match ${matchId}:`, error);
    throw error;
  }
}; 