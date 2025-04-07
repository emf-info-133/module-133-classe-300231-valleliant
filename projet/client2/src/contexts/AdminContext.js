import React, { createContext, useState, useEffect } from 'react';

// Créer le contexte
export const AdminContext = createContext();

// Composant fournisseur du contexte
export const AdminProvider = ({ children }) => {
  const [admin, setAdmin] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Vérifier si l'administrateur est déjà connecté
    const storedAdmin = localStorage.getItem('admin');
    if (storedAdmin) {
      setAdmin(JSON.parse(storedAdmin));
    }
    setLoading(false);
  }, []);

  // Fonction de connexion administrateur
  const login = async (email, password) => {
    try {
      // Simuler une requête API
      await new Promise(resolve => setTimeout(resolve, 800));
      
      // Vérification simplifiée (à remplacer par une vraie API)
      if (email === 'admin@example.com' && password === 'admin123') {
        const adminData = {
          id: 1,
          email: 'admin@example.com',
          name: 'Admin Principal',
          role: 'admin'
        };
        
        // Stocker dans le localStorage pour persistance
        localStorage.setItem('admin', JSON.stringify(adminData));
        setAdmin(adminData);
        return { success: true };
      } else {
        throw new Error('Identifiants invalides');
      }
    } catch (error) {
      return { success: false, error: error.message };
    }
  };

  // Fonction de déconnexion
  const logout = () => {
    localStorage.removeItem('admin');
    setAdmin(null);
  };

  // Valeur du contexte à partager
  const contextValue = {
    admin,
    loading,
    login,
    logout,
    isAuthenticated: !!admin
  };

  return (
    <AdminContext.Provider value={contextValue}>
      {children}
    </AdminContext.Provider>
  );
}; 