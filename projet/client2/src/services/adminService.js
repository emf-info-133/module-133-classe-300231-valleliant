import axios from 'axios';
import { API_URL } from '../config';

// URL de base pour l'authentification et l'administration
const AUTH_API = `${API_URL}/service2/auth`;
const ADMIN_API = `${API_URL}/service2/admin`;

// Login administrateur
export const adminLogin = async (credentials) => {
  try {
    const response = await axios.post(`${AUTH_API}/admin/login`, credentials, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    // Stockage du token dans localStorage
    if (response.data.token) {
      localStorage.setItem('adminToken', response.data.token);
      localStorage.setItem('adminUser', JSON.stringify(response.data.admin));
    }
    
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la connexion:', error);
    throw error;
  }
};

// Déconnexion administrateur
export const adminLogout = () => {
  localStorage.removeItem('adminToken');
  localStorage.removeItem('adminUser');
};

// Récupérer le profil de l'administrateur connecté
export const getAdminProfile = async () => {
  try {
    const response = await axios.get(`${ADMIN_API}/profile`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération du profil administrateur:', error);
    throw error;
  }
};

// Vérifier si le token est valide
export const validateToken = async () => {
  try {
    const token = localStorage.getItem('adminToken');
    
    if (!token) {
      return false;
    }
    
    const response = await axios.get(`${AUTH_API}/validate`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    return response.data.valid === true;
  } catch (error) {
    console.error('Erreur lors de la validation du token:', error);
    return false;
  }
};

// Mise à jour du profil administrateur
export const updateAdminProfile = async (profileData) => {
  try {
    const response = await axios.put(`${ADMIN_API}/profile`, profileData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    
    // Mise à jour des données stockées si nécessaire
    if (response.data.admin) {
      localStorage.setItem('adminUser', JSON.stringify(response.data.admin));
    }
    
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la mise à jour du profil:', error);
    throw error;
  }
};

// Changer le mot de passe de l'administrateur
export const changeAdminPassword = async (passwordData) => {
  try {
    const response = await axios.put(`${ADMIN_API}/change-password`, passwordData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors du changement de mot de passe:', error);
    throw error;
  }
}; 