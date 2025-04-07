import React, { createContext, useState, useEffect } from 'react';
import { loginUser, registerUser, getUser } from '../services/authService';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      getUser(token)
        .then(userData => {
          setUser(userData);
        })
        .catch(err => {
          localStorage.removeItem('token');
        })
        .finally(() => {
          setLoading(false);
        });
    } else {
      setLoading(false);
    }
  }, []);

  const login = async (email, password) => {
    setError(null);
    try {
      const data = await loginUser(email, password);
      localStorage.setItem('token', data.token);
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
      localStorage.setItem('token', data.token);
      setUser(data.user);
      return data.user;
    } catch (err) {
      setError(err.message || 'Échec de l\'inscription');
      throw err;
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, error, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}; 