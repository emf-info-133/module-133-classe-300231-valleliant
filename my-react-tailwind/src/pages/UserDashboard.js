import React from 'react';
import { useAuth } from '../hooks/useAuth';
import { Link } from 'react-router-dom';

const UserDashboard = () => {
  const { user } = useAuth();

  return (
    <div>
      <h1 className="text-3xl font-bold text-indigo-400 mb-6">Mon Espace</h1>
      <p className="text-lg text-gray-300 mb-4">
        Bienvenue, {user?.name || user?.email} !
      </p>
      <p className="text-gray-400 mb-8">
        C'est ici que vous pourrez gérer vos informations, vos équipes et suivre les tournois.
      </p>

      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {/* Carte pour gérer le profil (à implémenter) */}
        <div className="bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 transition duration-150">
          <h2 className="text-xl font-semibold text-indigo-400 mb-3">Mon Profil</h2>
          <p className="text-gray-400 mb-4">Mettre à jour vos informations personnelles.</p>
          {/* <Link to="/profile" className="font-medium text-indigo-400 hover:text-indigo-300">Gérer mon profil &rarr;</Link> */} 
          <span className="text-gray-500">(Fonctionnalité à venir)</span>
        </div>

        {/* Carte pour gérer les équipes */}
        <Link to="/teams" className="block bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 transition duration-150">
          <h2 className="text-xl font-semibold text-indigo-400 mb-3">Mes Équipes</h2>
          <p className="text-gray-400 mb-4">Voir, créer ou rejoindre des équipes.</p>
          <span className="font-medium text-indigo-400 hover:text-indigo-300">Gérer mes équipes &rarr;</span>
        </Link>

         {/* Carte pour voir les tournois (à implémenter) */}
         <div className="bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 transition duration-150">
          <h2 className="text-xl font-semibold text-indigo-400 mb-3">Tournois</h2>
          <p className="text-gray-400 mb-4">Consulter les tournois disponibles et passés.</p>
          {/* <Link to="/tournaments" className="font-medium text-indigo-400 hover:text-indigo-300">Voir les tournois &rarr;</Link> */}
          <span className="text-gray-500">(Fonctionnalité à venir)</span>
        </div>
        
         {/* Carte pour voir les matchs (à implémenter) */}
        <div className="bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 transition duration-150">
          <h2 className="text-xl font-semibold text-indigo-400 mb-3">Matchs</h2>
          <p className="text-gray-400 mb-4">Suivre les résultats des matchs en cours.</p>
           {/* <Link to="/matches" className="font-medium text-indigo-400 hover:text-indigo-300">Voir les matchs &rarr;</Link> */} 
           <span className="text-gray-500">(Fonctionnalité à venir)</span>
        </div>

      </div>
    </div>
  );
};

export default UserDashboard; 