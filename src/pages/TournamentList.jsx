import React, { useState, useEffect, useContext } from 'react';
import { AuthContext } from '../contexts/AuthContext';
import { getAllTournaments } from '../services/tournamentService';
import TournamentCard from '../components/tournaments/TournamentCard';
import TeamCreationModal from '../components/teams/TeamCreationModal';
import { useNavigate } from 'react-router-dom';

const TournamentList = () => {
  const [tournaments, setTournaments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedTournament, setSelectedTournament] = useState(null);
  const [showTeamModal, setShowTeamModal] = useState(false);
  
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) {
      navigate('/login');
      return;
    }

    const fetchTournaments = async () => {
      try {
        const data = await getAllTournaments();
        setTournaments(data);
      } catch (err) {
        setError('Impossible de récupérer les tournois');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchTournaments();
  }, [user, navigate]);

  const handleCreateTeam = (tournament) => {
    setSelectedTournament(tournament);
    setShowTeamModal(true);
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-xl">Chargement des tournois...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100">
      <header className="bg-white shadow-md p-4">
        <div className="container mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold">Tournois</h1>
          <div className="flex items-center space-x-4">
            <span>Bonjour, {user?.name}</span>
            <button 
              onClick={handleLogout}
              className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition"
            >
              Déconnexion
            </button>
          </div>
        </div>
      </header>

      <main className="container mx-auto py-8">
        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            {error}
          </div>
        )}

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {tournaments.length > 0 ? (
            tournaments.map((tournament) => (
              <TournamentCard 
                key={tournament.id} 
                tournament={tournament} 
                onCreateTeam={() => handleCreateTeam(tournament)}
              />
            ))
          ) : (
            <p className="col-span-3 text-center text-gray-600 text-lg">
              Aucun tournoi disponible pour le moment.
            </p>
          )}
        </div>
      </main>

      {showTeamModal && selectedTournament && (
        <TeamCreationModal 
          tournament={selectedTournament}
          onClose={() => setShowTeamModal(false)}
        />
      )}
    </div>
  );
};

export default TournamentList; 