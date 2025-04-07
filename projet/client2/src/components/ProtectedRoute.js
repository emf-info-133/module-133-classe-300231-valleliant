import React, { useContext, useEffect } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { AdminContext } from '../contexts/AdminContext';

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useContext(AdminContext);
  const location = useLocation();

  // Si le chargement est en cours, on peut afficher un indicateur de chargement
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-100">
        <div className="text-center">
          <p className="text-gray-600">Chargement...</p>
        </div>
      </div>
    );
  }

  // Si l'utilisateur n'est pas authentifié, rediriger vers la page de connexion
  if (!isAuthenticated) {
    // On sauvegarde l'URL actuelle pour pouvoir y revenir après connexion
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Si l'utilisateur est authentifié, on affiche le contenu
  return children;
};

export default ProtectedRoute; 