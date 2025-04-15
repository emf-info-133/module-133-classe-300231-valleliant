import React from 'react';

const TeamList = ({ teams, onEdit, onDelete, currentUser, isAdmin }) => {

  if (!teams || teams.length === 0) {
    return <p className="text-center text-gray-400 mt-6">Aucune équipe trouvée.</p>;
  }

  return (
    <div className="bg-gray-800 shadow-lg rounded-lg overflow-hidden">
      <table className="min-w-full divide-y divide-gray-700">
        <thead className="bg-gray-700">
          <tr>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Nom de l'Équipe</th>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Capitaine ID</th> {/* Idéalement, afficher le nom du capitaine */}
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Jeu</th>
            {/* Ajoutez d'autres colonnes pertinentes si nécessaire */}
            <th scope="col" className="px-6 py-3 text-right text-xs font-medium text-gray-300 uppercase tracking-wider">Actions</th>
          </tr>
        </thead>
        <tbody className="bg-gray-800 divide-y divide-gray-700">
          {teams.map((team) => {
            const canEdit = isAdmin || team.captain === currentUser?.id;
            const canDelete = isAdmin || team.captain === currentUser?.id;
            
            return (
              <tr key={team.id} className="hover:bg-gray-700 transition duration-150">
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-indigo-400">{team.name}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-300">{team.captain}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-300">{team.game || 'N/A'}</td>
                {/* Ajoutez d'autres cellules */}
                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-2">
                  {/* <button onClick={() => alert('Voir détails de l\'équipe ' + team.id)} className="text-blue-400 hover:text-blue-300">Détails</button> */} 
                  {canEdit && (
                    <button 
                        onClick={() => onEdit(team)} 
                        className="text-yellow-400 hover:text-yellow-300"
                        aria-label={`Modifier ${team.name}`}
                    >
                        Modifier
                    </button>
                  )}
                  {canDelete && (
                    <button 
                        onClick={() => onDelete(team.id)} 
                        className="text-red-500 hover:text-red-400"
                        aria-label={`Supprimer ${team.name}`}
                    >
                        Supprimer
                    </button>
                  )}
                   {!canEdit && !canDelete && (
                       <span className="text-gray-500 text-xs italic">Aucune action</span>
                   )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default TeamList; 