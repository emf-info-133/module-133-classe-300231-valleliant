import React from 'react';
import { Link } from 'react-router-dom';

const TournamentCard = ({ tournament, onCreateTeam }) => {
  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden">
      <div className="p-6">
        <h3 className="text-xl font-semibold mb-2">{tournament.name}</h3>
        <p className="text-gray-600 mb-4">
          {tournament.teams?.length || 0} équipes participantes
        </p>
        
        <div className="flex justify-between mt-4">
          <Link 
            to={`/tournaments/${tournament.id}`}
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
          >
            Voir détails
          </Link>
          
          <button
            onClick={() => onCreateTeam(tournament)}
            className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition"
          >
            Créer une équipe
          </button>
        </div>
      </div>
    </div>
  );
};

export default TournamentCard; 