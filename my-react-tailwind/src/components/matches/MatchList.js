import React, { useState, useEffect } from 'react';
import * as api from '../../services/api'; // Pour récupérer les noms des équipes/tournois

const MatchList = ({ matches, onEdit, onDelete }) => {
  const [teams, setTeams] = useState({});
  const [tournaments, setTournaments] = useState({});
  const [loadingDetails, setLoadingDetails] = useState(true);

  // Charger les noms des équipes et tournois pour un affichage plus clair
  useEffect(() => {
    const fetchDetails = async () => {
      try {
        const [teamsRes, tournamentsRes] = await Promise.all([
          api.getAllTeams(),
          api.getAllTournaments(),
        ]);

        const teamsMap = (teamsRes.data || []).reduce((acc, team) => {
          acc[team.id] = team.name;
          return acc;
        }, {});
        setTeams(teamsMap);

        const tournamentsMap = (tournamentsRes.data || []).reduce((acc, t) => {
          acc[t.id] = t.name;
          return acc;
        }, {});
        setTournaments(tournamentsMap);

      } catch (error) {
        console.error("Erreur lors de la récupération des détails (équipes/tournois):", error);
        // Continuer même si les détails ne sont pas chargés
      } finally {
         setLoadingDetails(false);
      }
    };
    fetchDetails();
  }, []);


  if (!matches || matches.length === 0) {
    return <p className="text-center text-gray-400 mt-6">Aucun match trouvé.</p>;
  }

  return (
    <div className="bg-gray-800 shadow-lg rounded-lg overflow-hidden">
      <table className="min-w-full divide-y divide-gray-700">
        <thead className="bg-gray-700">
          <tr>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Tournoi</th>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Équipe 1</th>
             <th scope="col" className="px-6 py-3 text-center text-xs font-medium text-gray-300 uppercase tracking-wider">Score</th>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Équipe 2</th>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Date/Heure</th>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-300 uppercase tracking-wider">Statut</th>
            <th scope="col" className="px-6 py-3 text-right text-xs font-medium text-gray-300 uppercase tracking-wider">Actions</th>
          </tr>
        </thead>
        <tbody className="bg-gray-800 divide-y divide-gray-700">
          {matches.map((match) => (
            <tr key={match.id} className="hover:bg-gray-700 transition duration-150">
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-300">
                {loadingDetails ? '...' : tournaments[match.tournamentId] || `ID: ${match.tournamentId}`}
                </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-indigo-400">
                {loadingDetails ? '...' : teams[match.team1Id] || `ID: ${match.team1Id}`}
                </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-center font-semibold text-white">
                  {match.score1 ?? '-'} : {match.score2 ?? '-'}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-indigo-400">
                {loadingDetails ? '...' : teams[match.team2Id] || `ID: ${match.team2Id}`}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-300">
                {match.matchDate ? new Date(match.matchDate).toLocaleString() : 'N/A'}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-300">{match.status || 'Planifié'}</td>
              <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-2">
                 <button 
                    onClick={() => onEdit(match)} 
                    className="text-yellow-400 hover:text-yellow-300"
                    aria-label={`Modifier match ${match.id}`}
                 >
                    Modifier
                 </button>
                 <button 
                    onClick={() => onDelete(match.id)} 
                    className="text-red-500 hover:text-red-400"
                     aria-label={`Supprimer match ${match.id}`}
                 >
                    Supprimer
                 </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
       {loadingDetails && <p className="text-center text-xs text-gray-500 py-2">Chargement des détails...</p>}
    </div>
  );
};

export default MatchList; 