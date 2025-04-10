import axios from 'axios';
import { API_URL } from '../config';

// URL de base via la passerelle
const GATEWAY_API = `${API_URL}/gateway`;

// Récupérer tous les utilisateurs
export const getAllUsers = async () => {
  try {
    const response = await axios.get(`${GATEWAY_API}/users`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des utilisateurs:', error);
    throw error;
  }
};

// Récupérer un utilisateur par son ID
export const getUserById = async (id) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/users/${id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération de l'utilisateur avec l'ID ${id}:`, error);
    throw error;
  }
};

// Récupérer les utilisateurs d'une équipe
export const getUsersByTeam = async (teamId) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/teams/${teamId}/users`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération des utilisateurs de l'équipe ${teamId}:`, error);
    throw error;
  }
};

// Récupérer les utilisateurs par rôle
export const getUsersByRole = async (role) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/users/role/${role}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération des utilisateurs avec le rôle ${role}:`, error);
    throw error;
  }
};

// Rechercher des utilisateurs
export const searchUsers = async (query) => {
  try {
    const response = await axios.get(`${GATEWAY_API}/users/search`, {
      params: { query },
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la recherche d'utilisateurs avec la requête ${query}:`, error);
    throw error;
  }
};

// Créer un nouvel utilisateur
export const createUser = async (userData) => {
  try {
    const response = await axios.post(`${GATEWAY_API}/users`, userData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la création de l\'utilisateur:', error);
    throw error;
  }
};

// Mettre à jour un utilisateur
export const updateUser = async (id, userData) => {
  try {
    const response = await axios.put(`${GATEWAY_API}/users/${id}`, userData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la mise à jour de l'utilisateur avec l'ID ${id}:`, error);
    throw error;
  }
};

// Supprimer un utilisateur
export const deleteUser = async (id) => {
  try {
    const response = await axios.delete(`${GATEWAY_API}/users/${id}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la suppression de l'utilisateur avec l'ID ${id}:`, error);
    throw error;
  }
};