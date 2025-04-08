import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
const GATEWAY_API = `${API_URL}/gateway`;

// Récupérer le classement pour un tournoi spécifique
export const getRankingByTournament = async (tournamentId) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/tournaments/${tournamentId}/rankings`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du classement pour le tournoi ${tournamentId}:`, error);
    throw error;
  }
};

// Récupérer le classement d'une équipe spécifique
export const getTeamRanking = async (tournamentId, teamId) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/tournaments/${tournamentId}/rankings/teams/${teamId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du classement de l'équipe ${teamId}:`, error);
    throw error;
  }
};

// Mettre à jour le classement d'une équipe
export const updateTeamRanking = async (tournamentId, teamId, updateData) => {
  try {
    const response = await axios.put(`${GATEWAY_API}/tournaments/${tournamentId}/rankings/teams/${teamId}`, updateData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la mise à jour du classement de l'équipe ${teamId}:`, error);
    throw error;
  }
}; 