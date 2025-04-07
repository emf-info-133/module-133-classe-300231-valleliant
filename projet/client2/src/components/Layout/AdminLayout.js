import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { FaHome, FaTrophy, FaUsers, FaUsersCog, FaGamepad, FaSignOutAlt, FaChartLine } from 'react-icons/fa';
import { useAdmin } from '../../context/AdminContext';
import '../../styles/AdminLayout.css';

const AdminLayout = ({ children }) => {
  const { admin, logout, isAuthenticated } = useAdmin();
  const navigate = useNavigate();
  const location = useLocation();
  const [pageTitle, setPageTitle] = useState('Tableau de bord');
  
  // Rediriger vers la page de connexion si non authentifié
  useEffect(() => {
    if (!isAuthenticated()) {
      navigate('/admin/login');
    }
  }, [isAuthenticated, navigate]);
  
  // Déterminer le titre de la page en fonction de l'URL
  useEffect(() => {
    const path = location.pathname;
    
    if (path.includes('/tournaments')) {
      setPageTitle('Gestion des Tournois');
    } else if (path.includes('/teams')) {
      setPageTitle('Gestion des Équipes');
    } else if (path.includes('/users')) {
      setPageTitle('Gestion des Utilisateurs');
    } else if (path.includes('/matches')) {
      setPageTitle('Gestion des Matchs');
    } else if (path.includes('/rankings')) {
      setPageTitle('Classements');
    } else if (path.includes('/dashboard')) {
      setPageTitle('Tableau de bord');
    } else if (path.includes('/profile')) {
      setPageTitle('Profil Administrateur');
    }
  }, [location]);
  
  const handleLogout = () => {
    logout();
    navigate('/admin/login');
  };
  
  return (
    <div className="admin-layout">
      <aside className="admin-sidebar">
        <div className="sidebar-logo">
          <h2>Admin Panel</h2>
        </div>
        
        <div className="admin-info">
          {admin && (
            <>
              <div className="admin-avatar">
                <span>{admin.firstName?.charAt(0) || 'A'}</span>
              </div>
              <div className="admin-details">
                <p className="admin-name">{admin.firstName} {admin.lastName}</p>
                <p className="admin-email">{admin.email}</p>
              </div>
            </>
          )}
        </div>
        
        <nav className="sidebar-nav">
          <ul>
            <li>
              <Link to="/admin/dashboard" className={location.pathname === '/admin/dashboard' ? 'active' : ''}>
                <FaHome /> <span>Tableau de bord</span>
              </Link>
            </li>
            <li>
              <Link to="/admin/tournaments" className={location.pathname.includes('/tournaments') ? 'active' : ''}>
                <FaTrophy /> <span>Tournois</span>
              </Link>
            </li>
            <li>
              <Link to="/admin/teams" className={location.pathname.includes('/teams') ? 'active' : ''}>
                <FaUsersCog /> <span>Équipes</span>
              </Link>
            </li>
            <li>
              <Link to="/admin/users" className={location.pathname.includes('/users') ? 'active' : ''}>
                <FaUsers /> <span>Utilisateurs</span>
              </Link>
            </li>
            <li>
              <Link to="/admin/matches" className={location.pathname.includes('/matches') ? 'active' : ''}>
                <FaGamepad /> <span>Matchs</span>
              </Link>
            </li>
            <li>
              <Link to="/admin/rankings" className={location.pathname.includes('/rankings') ? 'active' : ''}>
                <FaChartLine /> <span>Classements</span>
              </Link>
            </li>
          </ul>
        </nav>
        
        <div className="sidebar-footer">
          <button onClick={handleLogout} className="logout-btn">
            <FaSignOutAlt /> <span>Déconnexion</span>
          </button>
        </div>
      </aside>
      
      <main className="admin-content">
        <header className="admin-header">
          <h1>{pageTitle}</h1>
        </header>
        
        <div className="content-wrapper">
          {children}
        </div>
      </main>
    </div>
  );
};

export default AdminLayout; 