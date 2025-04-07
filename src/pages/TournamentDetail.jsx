import React, { useState, useEffect, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';
import { getTournamentById } from '../services/tournamentService';
import { getTeamsByTournament, joinTeam } from '../services/teamService';

const TournamentDetail = () => {
  const { tournamentId } = useParams();
  const [tournament, setTournament] = useState(null);
  const [teams, setTeams] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [joinLoading, setJoinLoading] = useState(false);
  
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) {
      navigate('/login');
      return;
    }

    const fetchTournamentData = async () => {
      try {
        const tournamentData = await getTournamentById(tournamentId);
        setTournament(tournamentData);
        
        const teamsData = await getTeamsByTournament(tournamentId);
        setTeams(teamsData);
      } catch (err) {
        setError('Impossible de récupérer les informations du tournoi');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchTournamentData();
  }, [tournamentId, user, navigate]);

  const handleJoinTeam = async (teamId) => {
    setJoinLoading(true);
    try {
      await joinTeam(teamId);
      // Rafraîchir la liste des équipes
      const teamsData = await getTeamsByTournament(tournamentId);
      setTeams(teamsData);
    } catch (err) {
      setError('Impossible de rejoindre l\'équipe');
      console.error(err);
    } finally {
      setJoinLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-xl">Chargement du tournoi...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100">
      <header className="bg-white shadow-md p-4">
        <div className="container mx-auto">
          <h1 className="text-2xl font-bold">{tournament?.name || 'Détails du tournoi'}</h1>
        </div>
      </header>

      <main className="container mx-auto py-8">
        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            {error}
          </div>
        )}

        <div className="bg-white rounded-lg shadow p-6 mb-8">
          <h2 className="text-xl font-semibold mb-4">Équipes participantes</h2>
          
          {teams.length > 0 ? (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {teams.map((team) => (
                <div key={team.id} className="border rounded p-4">
                  <h3 className="font-medium text-lg mb-2">{team.name}</h3>
                  <p className="text-gray-600 mb-2">
                    Capitaine: {team.captain.name}
                  </p>
                  <p className="text-gray-600 mb-4">
                    Membres: {team.members.length}/{team.maxMembers || 5}
                  </p>
                  
                  {!team.members.some(member => member.id === user.id) && (
                    <button
                      onClick={() => handleJoinTeam(team.id)}
                      disabled={joinLoading}
                      className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 transition disabled:opacity-50"
                    >
                      {joinLoading ? 'Traitement...' : 'Rejoindre'}
                    </button>
                  )}
                </div>
              ))}
            </div>
          ) : (
            <p className="text-gray-600">
              Aucune équipe n'a encore été créée pour ce tournoi.
            </p>
          )}
        </div>
      </main>
    </div>
  );
};

export default TournamentDetail; 