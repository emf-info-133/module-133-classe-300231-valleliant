import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Login from './pages/Login';
import TournamentList from './pages/TournamentList';
import TournamentDetail from './pages/TournamentDetail';
import './styles/App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/tournaments" element={<TournamentList />} />
          <Route path="/tournaments/:tournamentId" element={<TournamentDetail />} />
          <Route path="/" element={<Navigate to="/tournaments" replace />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App; 