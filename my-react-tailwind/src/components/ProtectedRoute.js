import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    // Affichez un indicateur de chargement pendant que l'état d'authentification est vérifié
    return <div>Chargement...</div>; // Ou un composant Spinner
  }

  if (!isAuthenticated) {
    // Redirige vers la page de connexion si non authentifié
    return <Navigate to="/login" replace />;
  }

  // Si authentifié, affiche le composant enfant (ou Outlet pour les routes imbriquées)
  return children ? children : <Outlet />;
};

export default ProtectedRoute; 