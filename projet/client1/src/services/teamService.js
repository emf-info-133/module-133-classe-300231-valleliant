import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
const GATEWAY_API = `${API_URL}/gateway`;

export const getTeamsByTournament = async (tournamentId) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/tournaments/${tournamentId}/teams`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération des équipes pour le tournoi ${tournamentId}:`, error);
    throw error;
  }
};

export const getTeamById = async (teamId) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/teams/${teamId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération de l'équipe ${teamId}:`, error);
    throw error;
  }
};

export const joinTeam = async (teamId) => {
  try {
    // Récupérer l'utilisateur depuis le localStorage
    const userStr = localStorage.getItem('user');
    if (!userStr) {
      throw new Error('Vous devez être connecté pour rejoindre une équipe');
    }
    
    const user = JSON.parse(userStr);
    
    const response = await axios.post(`${GATEWAY_API}/teams/${teamId}/join`, {
      userId: user.id
    }, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la tentative de rejoindre l'équipe ${teamId}:`, error);
    throw error;
  }
};

export const createTeam = async (tournamentId, teamName) => {
  try {
    // Récupérer l'utilisateur depuis le localStorage
    const userStr = localStorage.getItem('user');
    if (!userStr) {
      throw new Error('Vous devez être connecté pour créer une équipe');
    }
    
    const user = JSON.parse(userStr);
    
    const response = await axios.post(`${GATEWAY_API}/teams`, {
      name: teamName,
      captainId: user.id,
      tournamentId: tournamentId
    }, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la création de l\'équipe:', error);
    throw error;
  }
}; 