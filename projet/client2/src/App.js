import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './styles/App.css';

// Import des composants publics
import Home from './pages/Home';
import TournamentDetails from './pages/tournaments/TournamentDetails';
import NotFound from './pages/NotFound';

// Import des composants d'administration
import AdminLogin from './pages/AdminLogin';
import Dashboard from './pages/Dashboard';
import TournamentList from './pages/tournaments/TournamentList';
import CreateTournament from './pages/tournaments/CreateTournament';
import EditTournament from './pages/tournaments/EditTournament';
import TeamList from './pages/admin/TeamList';
import MatchList from './pages/admin/MatchList';
import RankingList from './pages/admin/RankingList';

// Import du contexte et des composants de mise en page
import { AdminProvider } from './context/AdminContext';
import AdminLayout from './components/Layout/AdminLayout';

// Composant pour les routes protégées
const ProtectedRoute = ({ children }) => {
  const isAuthenticated = localStorage.getItem('adminToken') !== null;
  
  if (!isAuthenticated) {
    return <Navigate to="/admin/login" />;
  }
  
  return children;
};

function App() {
  return (
    <AdminProvider>
      <Router>
        <Routes>
          {/* Routes publiques */}
          <Route path="/" element={<Home />} />
          <Route path="/tournament/:id" element={<TournamentDetails />} />
          <Route path="/admin/login" element={<AdminLogin />} />
          
          {/* Routes protégées d'administration */}
          <Route 
            path="/admin" 
            element={
              <ProtectedRoute>
                <AdminLayout>
                  <Dashboard />
                </AdminLayout>
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/admin/dashboard" 
            element={
              <ProtectedRoute>
                <AdminLayout>
                  <Dashboard />
                </AdminLayout>
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/admin/tournaments" 
            element={
              <ProtectedRoute>
                <AdminLayout>
                  <TournamentList />
                </AdminLayout>
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/admin/tournaments/create" 
            element={
              <ProtectedRoute>
                <AdminLayout>
                  <CreateTournament />
                </AdminLayout>
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/admin/tournaments/edit/:id" 
            element={
              <ProtectedRoute>
                <AdminLayout>
                  <EditTournament />
                </AdminLayout>
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/admin/teams" 
            element={
              <ProtectedRoute>
                <AdminLayout>
                  <TeamList />
                </AdminLayout>
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/admin/matches" 
            element={
              <ProtectedRoute>
                <AdminLayout>
                  <MatchList />
                </AdminLayout>
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/admin/rankings" 
            element={
              <ProtectedRoute>
                <AdminLayout>
                  <RankingList />
                </AdminLayout>
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