import React from 'react';

const TournamentList = ({ tournaments, onEdit, onDelete }) => {

  if (!tournaments || tournaments.length === 0) {
    return <p className="text-center text-gray-400 mt-6">Aucun tournoi trouvé.</p>;
  }

  return (
    <div className="bg-gray-800 shadow-lg rounded-lg overflow-hidden">
      <table className="min-w-full divide-y divide-gray-700">
        <thead className="bg-gray-700">
          <tr>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Nom du Tournoi</th>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Jeu</th>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Date de Début</th>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Statut</th>
             {/* Ajoutez d'autres colonnes si nécessaire (ex: nombre d'équipes) */}
            <th scope="col" className="px-6 py-3 text-right text-xs font-medium text-gray-300 uppercase tracking-wider">Actions</th>
          </tr>
        </thead>
        <tbody className="bg-gray-800 divide-y divide-gray-700">
          {tournaments.map((tournament) => (
            <tr key={tournament.id} className="hover:bg-gray-700 transition duration-150">
              <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-yellow-400">{tournament.name}</td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-300">{tournament.game}</td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-300">
                {tournament.startDate ? new Date(tournament.startDate).toLocaleDateString() : 'N/A'}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-300">{tournament.status || 'Planifié'}</td>
              {/* Ajoutez d'autres cellules */}
              <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-2">
                 {/* Ajouter un bouton "Voir" pour les détails */}
                 {/* <button onClick={() => alert('Voir détails du tournoi ' + tournament.id)} className="text-blue-400 hover:text-blue-300">Voir</button> */} 
                 <button 
                    onClick={() => onEdit(tournament)} 
                    className="text-yellow-400 hover:text-yellow-300"
                    aria-label={`Modifier ${tournament.name}`}
                 >
                    Modifier
                 </button>
                 <button 
                    onClick={() => onDelete(tournament.id)} 
                    className="text-red-500 hover:text-red-400"
                    aria-label={`Supprimer ${tournament.name}`}
                 >
                    Supprimer
                 </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TournamentList; 