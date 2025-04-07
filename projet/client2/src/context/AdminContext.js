import React, { createContext, useState, useEffect, useContext } from 'react';
import { adminLogin, adminLogout, validateToken, getAdminProfile } from '../services/adminService';

// Création du contexte
export const AdminContext = createContext();

// Hook personnalisé pour utiliser le contexte admin
export const useAdmin = () => useContext(AdminContext);

// Fournisseur du contexte
export const AdminProvider = ({ children }) => {
  const [admin, setAdmin] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Vérifier le token au chargement
  useEffect(() => {
    const checkToken = async () => {
      setLoading(true);
      try {
        // Récupérer l'admin depuis le stockage local
        const storedAdmin = localStorage.getItem('adminUser');
        
        if (storedAdmin) {
          const parsedAdmin = JSON.parse(storedAdmin);
          const isValid = await validateToken();
          
          if (isValid) {
            // Si le token est valide, récupérer les données à jour de l'admin
            try {
              const profile = await getAdminProfile();
              setAdmin(profile);
            } catch (profileError) {
              // Si erreur lors de la récupération du profil, utiliser les données stockées
              setAdmin(parsedAdmin);
            }
          } else {
            // Si le token n'est pas valide, déconnecter
            adminLogout();
            setAdmin(null);
          }
        } else {
          setAdmin(null);
        }
      } catch (err) {
        console.error('Erreur lors de la vérification de l\'authentification:', err);
        setError('Erreur d\'authentification');
        setAdmin(null);
      } finally {
        setLoading(false);
      }
    };
    
    checkToken();
  }, []);
  
  // Fonction pour se connecter
  const login = async (credentials) => {
    setLoading(true);
    setError(null);
    
    try {
      const data = await adminLogin(credentials);
      setAdmin(data.admin);
      return data;
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la connexion');
      throw err;
    } finally {
      setLoading(false);
    }
  };
  
  // Fonction pour se déconnecter
  const logout = () => {
    adminLogout();
    setAdmin(null);
  };
  
  // Vérifier si l'utilisateur est authentifié
  const isAuthenticated = () => {
    return !!admin;
  };
  
  // Valeurs à exposer dans le contexte
  const value = {
    admin,
    loading,
    error,
    login,
    logout,
    isAuthenticated
  };
  
  return (
    <AdminContext.Provider value={value}>
      {children}
    </AdminContext.Provider>
  );
}; 