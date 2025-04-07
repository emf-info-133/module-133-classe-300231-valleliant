import axios from 'axios';
import { API_URL } from '../config';

// URL de base pour les tournois
const TOURNAMENT_API = `${API_URL}/service1/tournaments`;

// Récupérer tous les tournois
export const getAllTournaments = async () => {
  try {
    const response = await axios.get(TOURNAMENT_API, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des tournois:', error);
    throw error;
  }
};

// Récupérer les tournois gérés par un administrateur
export const getTournamentsByAdmin = async (adminId) => {
  try {
    const response = await axios.get(`${TOURNAMENT_API}/admin/${adminId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération des tournois de l'administrateur ${adminId}:`, error);
    throw error;
  }
};

// Récupérer un tournoi par son ID
export const getTournamentById = async (id) => {
  try {
    const response = await axios.get(`${TOURNAMENT_API}/${id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du tournoi avec l'ID ${id}:`, error);
    throw error;
  }
};

// Créer un nouveau tournoi
export const createTournament = async (tournamentData) => {
  try {
    const response = await axios.post(TOURNAMENT_API, tournamentData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la création du tournoi:', error);
    throw error;
  }
};

// Mettre à jour un tournoi
export const updateTournament = async (id, tournamentData) => {
  try {
    const response = await axios.put(`${TOURNAMENT_API}/${id}`, tournamentData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la mise à jour du tournoi avec l'ID ${id}:`, error);
    throw error;
  }
};

// Supprimer un tournoi
export const deleteTournament = async (id) => {
  try {
    const response = await axios.delete(`${TOURNAMENT_API}/${id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la suppression du tournoi avec l'ID ${id}:`, error);
    throw error;
  }
}; 