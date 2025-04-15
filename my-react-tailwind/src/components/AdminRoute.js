import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const AdminRoute = ({ children }) => {
  const { isAuthenticated, isAdmin, loading } = useAuth();

  if (loading) {
    return <div>Chargement...</div>; // Ou un composant Spinner
  }

  if (!isAuthenticated) {
    // Si pas connecté, redirige vers login
    return <Navigate to="/login" replace />;
  }

  if (!isAdmin) {
    // Si connecté mais pas admin, redirige vers le tableau de bord utilisateur (ou une page 403)
    console.log("Accès refusé: Admin requis.");
    return <Navigate to="/dashboard" replace />; // Ou une page d'erreur dédiée
  }

  // Si authentifié et admin, affiche le contenu
  return children ? children : <Outlet />;
};

export default AdminRoute; 