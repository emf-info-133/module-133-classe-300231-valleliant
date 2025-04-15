import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import ProtectedRoute from '../components/ProtectedRoute';
import AdminRoute from '../components/AdminRoute';

// Importer les pages (à créer)
import LoginPage from '../pages/LoginPage';
import RegisterPage from '../pages/RegisterPage';
import UserDashboard from '../pages/UserDashboard';
import AdminDashboard from '../pages/AdminDashboard';
import TeamManagementPage from '../pages/TeamManagementPage'; // Pourra être utilisé par User et Admin
import TournamentManagementPage from '../pages/TournamentManagementPage'; // Probablement Admin
import MatchManagementPage from '../pages/MatchManagementPage'; // Probablement Admin
import UserManagementPage from '../pages/UserManagementPage'; // Admin
import HomePage from '../pages/HomePage'; // Page d'accueil publique
import NotFoundPage from '../pages/NotFoundPage'; // Page 404

// Composant Layout pour Admin (si nécessaire)
// import AdminLayout from '../components/layout/AdminLayout';

const AppRoutes = () => {
  return (
    <Routes>
      {/* Routes Publiques */}
      <Route path="/" element={<HomePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      {/* Routes Protégées (Utilisateurs connectés) */}
      <Route element={<ProtectedRoute />}>
        <Route path="/dashboard" element={<UserDashboard />} />
        <Route path="/teams" element={<TeamManagementPage />} />
        {/* Ajoutez d'autres routes utilisateur ici (ex: profil) */}
      </Route>

      {/* Routes Administrateur (Nécessite AdminRoute) */}
      <Route path="/admin" element={<AdminRoute />}>
        {/* Option 1: Routes admin directes */}
        {/* <Route index element={<AdminDashboard />} /> /* /admin */}
        {/* <Route path="dashboard" element={<AdminDashboard />} /> /* /admin/dashboard */}
        {/* <Route path="users" element={<UserManagementPage />} />
        <Route path="tournaments" element={<TournamentManagementPage />} />
        <Route path="matches" element={<MatchManagementPage />} /> */}
        
        {/* Option 2: Utilisation d'un Outlet dans AdminRoute et définir les sous-routes */}
        <Route index element={<Navigate to="dashboard" replace />} /> {/* Redirige /admin vers /admin/dashboard */}
        <Route path="dashboard" element={<AdminDashboard />} />
        <Route path="users" element={<UserManagementPage />} />
        <Route path="teams" element={<TeamManagementPage />} /> {/* Les admins peuvent aussi voir/gérer les équipes */}
        <Route path="tournaments" element={<TournamentManagementPage />} />
        <Route path="matches" element={<MatchManagementPage />} />
      </Route>

      {/* Route 404 */}
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
};

export default AppRoutes; 