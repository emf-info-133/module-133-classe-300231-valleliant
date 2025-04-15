import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const HomePage = () => {
  const { isAuthenticated, isAdmin } = useAuth();

  return (
    <div className="text-center">
      <h1 className="text-4xl font-bold text-indigo-400 mb-6">Bienvenue sur TournoiApp !</h1>
      <p className="text-lg text-gray-300 mb-8">
        Votre plateforme pour gérer et participer à des tournois.
      </p>
      
      <div className="space-y-4 md:space-y-0 md:space-x-4">
        {!isAuthenticated ? (
          <>
            <Link to="/login" className="inline-block btn-primary text-lg px-6 py-3"> 
              Se connecter
            </Link>
            <Link to="/register" className="inline-block btn-secondary text-lg px-6 py-3">
              Créer un compte
            </Link>
          </>
        ) : (
          <>
            <Link to="/dashboard" className="inline-block btn-primary text-lg px-6 py-3">
              Accéder à mon espace
            </Link>
            {isAdmin && (
                <Link to="/admin" className="inline-block btn-secondary text-lg px-6 py-3">
                  Panneau d'administration
                </Link>
            )}
             <Link to="/teams" className="inline-block btn-secondary text-lg px-6 py-3">
              Voir les équipes
            </Link>
            {/* Ajouter un lien vers les tournois ? */}
          </>
        )}
      </div>

      {/* Section d'information ou de fonctionnalités (optionnel) */}
      <div className="mt-16 grid md:grid-cols-3 gap-8 text-left">
          <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
              <h2 className="text-2xl font-semibold text-indigo-400 mb-3">Gestion d'Équipes</h2>
              <p className="text-gray-400">Créez ou rejoignez des équipes pour participer aux compétitions.</p>
          </div>
          <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
              <h2 className="text-2xl font-semibold text-indigo-400 mb-3">Tournois Organisés</h2>
              <p className="text-gray-400">Consultez les tournois à venir et inscrivez votre équipe.</p>
          </div>
          <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
              <h2 className="text-2xl font-semibold text-indigo-400 mb-3">Suivi des Matchs</h2>
              <p className="text-gray-400">Suivez les résultats et le classement en temps réel.</p>
          </div>
      </div>
    </div>
  );
};

export default HomePage; 