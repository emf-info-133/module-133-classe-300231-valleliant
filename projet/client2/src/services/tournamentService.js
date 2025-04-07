import axios from 'axios';
import { API_URL } from '../config';

// Données fictives pour les tournois
let mockTournaments = [
  {
    id: 1,
    name: 'Tournoi de Printemps 2023',
    description: 'Tournoi saisonnier pour les joueurs de tous niveaux',
    startDate: '2023-05-15',
    endDate: '2023-06-15',
    adminId: 1,
    gameId: 1,
    status: 'active'
  },
  {
    id: 2,
    name: 'Championnat régional',
    description: 'Compétition officielle pour les joueurs qualifiés',
    startDate: '2023-07-01',
    endDate: '2023-07-15',
    adminId: 1,
    gameId: 2,
    status: 'upcoming'
  },
  {
    id: 3,
    name: 'Coupe des Champions',
    description: 'Tournoi prestigieux réservé aux meilleures équipes',
    startDate: '2023-09-10',
    endDate: '2023-10-05',
    adminId: 1,
    gameId: 1,
    status: 'upcoming'
  }
];

// Récupérer tous les tournois
export const getAllTournaments = async () => {
  try {
    const response = await axios.get(`${API_URL}/tournaments`, {
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
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 500));
  return mockTournaments.filter(t => t.adminId === adminId);
};

// Récupérer un tournoi par son ID
export const getTournamentById = async (id) => {
  try {
    const response = await axios.get(`${API_URL}/tournaments/${id}`, {
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
    const response = await axios.post(`${API_URL}/tournaments`, tournamentData, {
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
    const response = await axios.put(`${API_URL}/tournaments/${id}`, tournamentData, {
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
    const response = await axios.delete(`${API_URL}/tournaments/${id}`, {
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