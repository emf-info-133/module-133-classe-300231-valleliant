import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

const Navbar = () => {
  const { isAuthenticated, isAdmin, logout, user } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logout();
      navigate('/login'); // Rediriger vers la page de connexion après déconnexion
    } catch (error) {
      console.error("Erreur lors de la déconnexion depuis Navbar:", error);
      // Afficher une notification d'erreur si nécessaire
    }
  };

  return (
    <nav className="bg-gray-800 shadow-md">
      <div className="container mx-auto px-4">
        <div className="flex justify-between items-center py-4">
          {/* Logo/Nom du site */}
          <Link to="/" className="text-2xl font-bold text-indigo-400 hover:text-indigo-300 transition duration-150">
            TournoiApp
          </Link>

          {/* Liens de navigation */}
          <div className="flex items-center space-x-4">
            <Link to="/" className="text-gray-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium">Accueil</Link>

            {isAuthenticated ? (
              <>
                <Link to="/dashboard" className="text-gray-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium">Mon Espace</Link>
                <Link to="/teams" className="text-gray-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium">Équipes</Link>
                {/* Ajoutez d'autres liens pour utilisateurs connectés ici */}
                
                {isAdmin && (
                  <Link to="/admin" className="text-yellow-400 hover:text-yellow-300 px-3 py-2 rounded-md text-sm font-medium">Admin</Link>
                )}

                <span className="text-gray-400 text-sm hidden md:block">Bonjour, {user?.name || user?.email}</span>
                
                <button
                  onClick={handleLogout}
                  className="bg-red-600 hover:bg-red-700 text-white px-3 py-2 rounded-md text-sm font-medium transition duration-150"
                >
                  Déconnexion
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="text-gray-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium">Connexion</Link>
                <Link to="/register" className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-2 rounded-md text-sm font-medium transition duration-150">Inscription</Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar; 