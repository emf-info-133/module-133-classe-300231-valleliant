const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export const loginUser = async (email, password) => {
  try {
    const response = await fetch(`${API_URL}/users/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Échec de la connexion');
    }

    return await response.json();
  } catch (error) {
    throw error;
  }
};

export const registerUser = async (name, email, password) => {
  try {
    const response = await fetch(`${API_URL}/users`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ name, email, password }),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Échec de l\'inscription');
    }

    return await response.json();
  } catch (error) {
    throw error;
  }
};

export const getUser = async (token) => {
  try {
    const response = await fetch(`${API_URL}/users/me`, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error('Session expirée');
    }

    return await response.json();
  } catch (error) {
    throw error;
  }
}; 