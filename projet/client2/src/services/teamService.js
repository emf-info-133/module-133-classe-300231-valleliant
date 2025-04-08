import axios from 'axios';
import { API_URL } from '../config';

// URL de base via la passerelle
const GATEWAY_API = `${API_URL}/gateway`;

// Récupérer toutes les équipes
export const getAllTeams = async () => {
  try {
    const response = await axios.get(`${GATEWAY_API}/teams`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des équipes:', error);
    throw error;
  }
};

// Récupérer les équipes d'un tournoi
export const getTeamsByTournament = async (tournamentId) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/tournaments/${tournamentId}/teams`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération des équipes du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

// Récupérer une équipe par son ID
export const getTeamById = async (id) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/teams/${id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération de l'équipe avec l'ID ${id}:`, error);
    throw error;
  }
};

// Créer une nouvelle équipe
export const createTeam = async (teamData) => {
  try {
    const response = await axios.post(`${GATEWAY_API}/teams`, teamData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la création de l\'équipe:', error);
    throw error;
  }
};

// Mettre à jour une équipe
export const updateTeam = async (id, teamData) => {
  try {
    const response = await axios.put(`${GATEWAY_API}/teams/${id}`, teamData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la mise à jour de l'équipe avec l'ID ${id}:`, error);
    throw error;
  }
};

// Supprimer une équipe
export const deleteTeam = async (id) => {
  try {
    const response = await axios.delete(`${GATEWAY_API}/teams/${id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la suppression de l'équipe avec l'ID ${id}:`, error);
    throw error;
  }
}; 