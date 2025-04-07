import React, { createContext, useState, useEffect } from 'react';
import { loginUser, registerUser, getUser } from '../services/authService';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Essayer de récupérer l'utilisateur depuis le localStorage
    const storedUser = localStorage.getItem('user');
    const token = localStorage.getItem('token');
    
    if (storedUser && token) {
      try {
        setUser(JSON.parse(storedUser));
        setLoading(false);
      } catch (err) {
        // Si les données sont corrompues, nettoyer le localStorage
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setLoading(false);
      }
    } else {
      setLoading(false);
    }
  }, []);

  const login = async (email, password) => {
    setError(null);
    try {
      const data = await loginUser(email, password);
      
      // Stocker le token et l'utilisateur dans le localStorage
      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data.user));
      
      setUser(data.user);
      return data.user;
    } catch (err) {
      setError(err.message || 'Échec de la connexion');
      throw err;
    }
  };

  const register = async (name, email, password) => {
    setError(null);
    try {
      const data = await registerUser(name, email, password);
      
      // Stocker le token et l'utilisateur dans le localStorage
      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data.user));
      
      setUser(data.user);
      return data.user;
    } catch (err) {
      setError(err.message || 'Échec de l\'inscription');
      throw err;
    }
  };

  const logout = () => {
    // Nettoyer le localStorage
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, error, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}; 