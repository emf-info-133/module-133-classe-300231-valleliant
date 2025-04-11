import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
const GATEWAY_API = `${API_URL}`;

export const loginUser = async (email, password) => {
  try {
    const response = await axios.post(`${GATEWAY_API}/auth/login`, {
      email,
      password
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    // Stocker le token et les informations utilisateur
    if (response.data.token) {
      localStorage.setItem('userToken', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data.user));
    }
    
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la connexion:', error);
    throw error;
  }
};

export const registerUser = async (name, email, password) => {
  try {
    const response = await axios.post(`${GATEWAY_API}/auth/register`, {
      name,
      email,
      password
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    // Stocker le token et les informations utilisateur
    if (response.data.token) {
      localStorage.setItem('userToken', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data.user));
    }
    
    return response.data;
  } catch (error) {
    console.error('Erreur lors de l\'inscription:', error);
    throw error;
  }
};

export const getUser = async () => {
  try {
    const token = localStorage.getItem('userToken');
    if (!token) {
      throw new Error('Aucun token trouvé');
    }
    
    const response = await axios.get(`${GATEWAY_API}/users/profile`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération du profil utilisateur:', error);
    throw error;
  }
};

export const logout = () => {
  localStorage.removeItem('userToken');
  localStorage.removeItem('user');
};

export const isAuthenticated = () => {
  return !!localStorage.getItem('userToken');
}; 