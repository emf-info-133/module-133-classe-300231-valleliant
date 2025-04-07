import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './styles/App.css';

// Import des composants publics
import Home from './pages/Home';
import TournamentDetails from './pages/tournaments/TournamentDetails';
import NotFound from './pages/NotFound';

// Import des composants d'administration
import AdminLogin from './pages/admin/AdminLogin';
import Dashboard from './pages/admin/Dashboard';
import TournamentList from './pages/admin/TournamentList';
import CreateTournament from './pages/tournaments/CreateTournament';
import EditTournament from './pages/tournaments/EditTournament';
import TeamList from './pages/admin/TeamList';
import MatchList from './pages/admin/MatchList';
import RankingList from './pages/admin/RankingList';

// Import du contexte et du composant de route protégée
import { AdminProvider } from './contexts/AdminContext';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <AdminProvider>
      <Router>
        <Routes>
          {/* Routes publiques */}
          <Route path="/" element={<Home />} />
          <Route path="/tournament/:id" element={<TournamentDetails />} />
          
          {/* Routes d'administration */}
          <Route path="/admin/login" element={<AdminLogin />} />
          
          {/* Routes protégées d'administration */}
          <Route 
            path="/admin" 
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/admin/dashboard" 
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/tournaments" 
            element={
              <ProtectedRoute>
                <TournamentList />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/tournaments/create" 
            element={
              <ProtectedRoute>
                <CreateTournament />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/tournaments/edit/:id" 
            element={
              <ProtectedRoute>
                <EditTournament />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/teams" 
            element={
              <ProtectedRoute>
                <TeamList />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/matches" 
            element={
              <ProtectedRoute>
                <MatchList />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/rankings" 
            element={
              <ProtectedRoute>
                <RankingList />
              </ProtectedRoute>
            } 
          />
          
          {/* Redirection par défaut pour les chemins d'administration non reconnus */}
          <Route path="/admin/*" element={<Navigate to="/admin/dashboard" />} />
          
          {/* Route de secours pour les chemins non reconnus */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </Router>
    </AdminProvider>
  );
}

export default App; 