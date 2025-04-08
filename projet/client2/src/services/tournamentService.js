import axios from 'axios';
import { API_URL } from '../config';

// URL de base pour les tournois via la passerelle
const GATEWAY_API = `${API_URL}/gateway`;

// Récupérer tous les tournois
export const getAllTournaments = async () => {
  try {
    const response = await axios.get(`${GATEWAY_API}/tournaments`, {
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
    const response = await axios.get(`${GATEWAY_API}/tournaments/admin/${adminId}`, {
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
    const response = await axios.get(`${GATEWAY_API}/tournaments/${id}`, {
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

// Récupérer un tournoi avec les informations de l'administrateur
export const getTournamentWithAdmin = async (id) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/tournaments/${id}/withAdmin`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du tournoi avec admin ${id}:`, error);
    throw error;
  }
};

// Créer un nouveau tournoi
export const createTournament = async (tournamentData) => {
  try {
    // Récupérer l'admin ID depuis le localStorage
    const adminStr = localStorage.getItem('adminUser');
    if (adminStr) {
      const admin = JSON.parse(adminStr);
      tournamentData.adminId = admin.id;
    }
    
    const response = await axios.post(`${GATEWAY_API}/tournaments`, tournamentData, {
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
    const response = await axios.put(`${GATEWAY_API}/tournaments/${id}`, tournamentData, {
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
    const response = await axios.delete(`${GATEWAY_API}/tournaments/${id}`, {
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