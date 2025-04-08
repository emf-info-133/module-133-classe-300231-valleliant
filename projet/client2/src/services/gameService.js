import axios from 'axios';
import { API_URL } from '../config';

// URL de base pour les jeux via la passerelle
const GATEWAY_API = `${API_URL}/gateway`;

// Récupérer tous les jeux
export const getAllGames = async () => {
  try {
    const response = await axios.get(`${GATEWAY_API}/games`, {
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
    const response = await axios.get(`${GATEWAY_API}/games/${id}`, {
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
    const response = await axios.post(`${GATEWAY_API}/games`, gameData, {
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
    const response = await axios.put(`${GATEWAY_API}/games/${id}`, gameData, {
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
    const response = await axios.delete(`${GATEWAY_API}/games/${id}`, {
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