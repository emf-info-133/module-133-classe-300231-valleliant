import React, { useContext } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { AdminContext } from '../../contexts/AdminContext';
import '../../styles/AdminLayout.css';

const AdminLayout = ({ children }) => {
  const { admin, logout } = useContext(AdminContext);
  const navigate = useNavigate();
  const location = useLocation();
  
  const handleLogout = () => {
    logout();
    navigate('/login');
  };
  
  // Vérifier si un lien est actif
  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };
  
  return (
    <div className="admin-layout">
      <aside className="admin-sidebar">
        <div className="sidebar-header">
          <h2>Admin Panel</h2>
        </div>
        
        <div className="admin-info">
          <div className="admin-avatar">
            {admin?.name?.charAt(0) || 'A'}
          </div>
          <div className="admin-details">
            <p className="admin-name">{admin?.name || 'Admin'}</p>
            <p className="admin-email">{admin?.email || 'admin@example.com'}</p>
          </div>
        </div>
        
        <nav className="sidebar-nav">
          <ul>
            <li className={isActive('/dashboard')}>
              <Link to="/dashboard">
                <i className="fas fa-tachometer-alt"></i>
                Tableau de bord
              </Link>
            </li>
            <li className={isActive('/tournaments') || location.pathname.includes('/tournament/')}>
              <Link to="/tournaments">
                <i className="fas fa-trophy"></i>
                Tournois
              </Link>
            </li>
            <li className={isActive('/teams') || location.pathname.includes('/team/')}>
              <Link to="/teams">
                <i className="fas fa-users"></i>
                Équipes
              </Link>
            </li>
            <li className={isActive('/matches') || location.pathname.includes('/match/')}>
              <Link to="/matches">
                <i className="fas fa-gamepad"></i>
                Matchs
              </Link>
            </li>
            <li className={isActive('/rankings')}>
              <Link to="/rankings">
                <i className="fas fa-list-ol"></i>
                Classements
              </Link>
            </li>
          </ul>
        </nav>
        
        <div className="sidebar-footer">
          <button onClick={handleLogout} className="logout-button">
            <i className="fas fa-sign-out-alt"></i>
            Déconnexion
          </button>
        </div>
      </aside>
      
      <div className="admin-content">
        <header className="admin-header">
          <h1>{getPageTitle(location.pathname)}</h1>
        </header>
        
        <main className="admin-main">
          {children}
        </main>
      </div>
    </div>
  );
};

// Fonction pour obtenir le titre de la page en fonction du chemin
const getPageTitle = (pathname) => {
  if (pathname === '/dashboard') return 'Tableau de bord';
  if (pathname === '/tournaments') return 'Gestion des tournois';
  if (pathname.includes('/tournament/new')) return 'Créer un tournoi';
  if (pathname.includes('/tournament/edit')) return 'Modifier le tournoi';
  if (pathname.includes('/tournament/')) return 'Détails du tournoi';
  if (pathname === '/teams') return 'Gestion des équipes';
  if (pathname.includes('/team/new')) return 'Créer une équipe';
  if (pathname.includes('/team/edit')) return 'Modifier l\'équipe';
  if (pathname.includes('/team/')) return 'Détails de l\'équipe';
  if (pathname === '/matches') return 'Gestion des matchs';
  if (pathname.includes('/match/new')) return 'Créer un match';
  if (pathname.includes('/match/edit')) return 'Modifier le match';
  if (pathname.includes('/match/')) return 'Détails du match';
  if (pathname === '/rankings') return 'Classements';
  
  return 'Administration';
};

export default AdminLayout; 