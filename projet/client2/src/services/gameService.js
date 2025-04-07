import axios from 'axios';
import { API_URL } from '../config';

// URL de base pour les jeux
const GAME_API = `${API_URL}/service1/games`;

// Récupérer tous les jeux
export const getAllGames = async () => {
  try {
    const response = await axios.get(GAME_API, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des jeux:', error);
    throw error;
  }
};

// Récupérer un jeu par son ID
export const getGameById = async (id) => {
  try {
    const response = await axios.get(`${GAME_API}/${id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du jeu avec l'ID ${id}:`, error);
    throw error;
  }
};

// Créer un nouveau jeu
export const createGame = async (gameData) => {
  try {
    const response = await axios.post(GAME_API, gameData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la création du jeu:', error);
    throw error;
  }
};

// Mettre à jour un jeu
export const updateGame = async (id, gameData) => {
  try {
    const response = await axios.put(`${GAME_API}/${id}`, gameData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la mise à jour du jeu avec l'ID ${id}:`, error);
    throw error;
  }
};

// Supprimer un jeu
export const deleteGame = async (id) => {
  try {
    const response = await axios.delete(`${GAME_API}/${id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la suppression du jeu avec l'ID ${id}:`, error);
    throw error;
  }
}; 