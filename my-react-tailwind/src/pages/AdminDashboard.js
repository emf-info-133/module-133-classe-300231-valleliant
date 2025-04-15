import React from 'react';
import { Link } from 'react-router-dom';

// Pourrait afficher des statistiques ou des raccourcis
const AdminDashboard = () => {
  return (
    <div>
      <h1 className="text-3xl font-bold text-yellow-400 mb-6">Panneau d'Administration</h1>
      <p className="text-lg text-gray-300 mb-8">
        Gestion globale de la plateforme TournoiApp.
      </p>

      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {/* Carte Gestion Utilisateurs */}
        <Link to="/admin/users" className="block bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 transition duration-150">
          <h2 className="text-xl font-semibold text-yellow-400 mb-3">Utilisateurs</h2>
          <p className="text-gray-400 mb-4">Voir la liste des utilisateurs inscrits.</p>
          <span className="font-medium text-yellow-400 hover:text-yellow-300">Gérer les utilisateurs &rarr;</span>
        </Link>

        {/* Carte Gestion Équipes */}
        <Link to="/admin/teams" className="block bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 transition duration-150">
          <h2 className="text-xl font-semibold text-yellow-400 mb-3">Équipes</h2>
          <p className="text-gray-400 mb-4">Voir et gérer toutes les équipes créées.</p>
          <span className="font-medium text-yellow-400 hover:text-yellow-300">Gérer les équipes &rarr;</span>
        </Link>

        {/* Carte Gestion Tournois */}
        <Link to="/admin/tournaments" className="block bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 transition duration-150">
          <h2 className="text-xl font-semibold text-yellow-400 mb-3">Tournois</h2>
          <p className="text-gray-400 mb-4">Créer, modifier ou supprimer des tournois.</p>
          <span className="font-medium text-yellow-400 hover:text-yellow-300">Gérer les tournois &rarr;</span>
        </Link>

        {/* Carte Gestion Matchs */}
        <Link to="/admin/matches" className="block bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 transition duration-150">
          <h2 className="text-xl font-semibold text-yellow-400 mb-3">Matchs</h2>
          <p className="text-gray-400 mb-4">Créer, modifier ou supprimer des matchs.</p>
          <span className="font-medium text-yellow-400 hover:text-yellow-300">Gérer les matchs &rarr;</span>
        </Link>

         {/* Carte Gestion Jeux (si applicable) */}
         {/* <Link to="/admin/games" className="block bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 transition duration-150">
          <h2 className="text-xl font-semibold text-yellow-400 mb-3">Jeux</h2>
          <p className="text-gray-400 mb-4">Gérer les jeux supportés par la plateforme.</p>
          <span className="font-medium text-yellow-400 hover:text-yellow-300">Gérer les jeux &rarr;</span>
        </Link> */}
      </div>
    </div>
  );
};

export default AdminDashboard; 