import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
const GATEWAY_API = `${API_URL}/gateway`;

export const getAllTournaments = async () => {
  try {
    const response = await axios.get(`${GATEWAY_API}/tournaments`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des tournois:', error);
    throw error;
  }
};

export const getTournamentById = async (tournamentId) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/tournaments/${tournamentId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

export const getTournamentWithAdmin = async (tournamentId) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/tournaments/${tournamentId}/withAdmin`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du tournoi avec admin ${tournamentId}:`, error);
    throw error;
  }
};

export const getOpenTournaments = async () => {
  try {
    // Les tournois ouverts ont status = true
    const response = await axios.get(`${GATEWAY_API}/tournaments/open`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des tournois ouverts:', error);
    throw error;
  }
};

export const getTournamentMatches = async (tournamentId) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/tournaments/${tournamentId}/matches`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération des matchs du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

export const getTournamentRankings = async (tournamentId) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/tournaments/${tournamentId}/rankings`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du classement du tournoi ${tournamentId}:`, error);
    throw error;
  }
}; 