import React, { useState, useEffect } from 'react';
import { useAuth } from '../hooks/useAuth';
import { Link } from 'react-router-dom';
import * as api from '../services/api';
import Modal from '../components/common/Modal';
import TournamentForm from '../components/tournaments/TournamentForm';
import MatchForm from '../components/matches/MatchForm';

const UserDashboard = () => {
  const { user } = useAuth();
  const [showJoinTeamModal, setShowJoinTeamModal] = useState(false);
  const [showTournamentModal, setShowTournamentModal] = useState(false);
  const [showMatchModal, setShowMatchModal] = useState(false);
  const [teams, setTeams] = useState([]);
  const [selectedTeamId, setSelectedTeamId] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    const fetchTeams = async () => {
      try {
        const response = await api.getAllTeams();
        setTeams(response.data || []);
      } catch (err) {
        console.error("Erreur lors de la récupération des équipes:", err);
      }
    };
    fetchTeams();
  }, []);

  const handleJoinTeam = async () => {
    if (!selectedTeamId) {
      setError("Veuillez sélectionner une équipe.");
      return;
    }

    // S'assurer que l'ID utilisateur existe avant de l'envoyer
    if (!user || !user.id) {
      setError('Utilisateur non connecté ou ID utilisateur manquant.');
      return;
    }

    setLoading(true);
    setError('');
    setSuccess('');

    try {
      // Conversion explicite en entiers
      const userId = parseInt(user.id, 10);
      const teamId = parseInt(selectedTeamId, 10);

      // Vérification des valeurs numériques
      if (isNaN(userId) || userId > 2147483647) {
        throw new Error('ID utilisateur invalide');
      }

      if (isNaN(teamId) || teamId > 2147483647) {
        throw new Error('ID équipe invalide');
      }

      const teamUserData = {
        userId: userId,
        teamId: teamId
      };
      
      // Utilisation d'un autre format pour la route, selon l'API documentée
      await api.joinTeam(teamUserData);
      setSuccess(`Vous avez rejoint l'équipe avec succès !`);
      setShowJoinTeamModal(false);
    } catch (err) {
      if (err.message) {
        setError(err.message);
      } else {
        setError(err.response?.data?.message || "Erreur lors de la tentative de rejoindre l'équipe.");
      }
      console.error("Erreur join team:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleSaveTournament = async (tournamentData) => {
    try {
      await api.createTournament(tournamentData);
      setSuccess("Le tournoi a été créé avec succès !");
      setShowTournamentModal(false);
    } catch (err) {
      console.error("Erreur création tournoi:", err);
      setError(err.response?.data?.message || "Erreur lors de la création du tournoi.");
    }
  };

  const handleSaveMatch = async (matchData) => {
    try {
      await api.createMatch(matchData);
      setSuccess("Le match a été créé avec succès !");
      setShowMatchModal(false);
    } catch (err) {
      console.error("Erreur création match:", err);
      setError(err.response?.data?.message || "Erreur lors de la création du match.");
    }
  };

  return (
    <div>
      <h1 className="text-3xl font-bold text-indigo-400 mb-6">Mon Espace</h1>
      <p className="text-lg text-gray-300 mb-4">
        Bienvenue, {user?.name || user?.email} !
      </p>
      <p className="text-gray-400 mb-8">
        C'est ici que vous pourrez gérer vos équipes et suivre les tournois.
      </p>

      {success && (
        <div className="bg-green-900 border border-green-600 text-green-200 p-3 rounded mb-6">
          {success}
        </div>
      )}

      {error && (
        <div className="bg-red-900 border border-red-600 text-red-200 p-3 rounded mb-6">
          {error}
        </div>
      )}

      <div className="grid md:grid-cols-2 lg:grid-cols-2 gap-6">
        {/* Carte pour gérer les équipes */}
        <Link to="/teams" className="block bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 transition duration-150">
          <h2 className="text-xl font-semibold text-indigo-400 mb-3">Mes Équipes</h2>
          <p className="text-gray-400 mb-4">Voir ou créer des équipes.</p>
          <span className="font-medium text-indigo-400 hover:text-indigo-300">Gérer mes équipes &rarr;</span>
        </Link>

        {/* Carte pour rejoindre une équipe */}
        <div 
          onClick={() => setShowJoinTeamModal(true)}
          className="bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 cursor-pointer transition duration-150"
        >
          <h2 className="text-xl font-semibold text-indigo-400 mb-3">Rejoindre une Équipe</h2>
          <p className="text-gray-400 mb-4">Rejoindre une équipe existante.</p>
          <span className="font-medium text-indigo-400 hover:text-indigo-300">Rejoindre une équipe &rarr;</span>
        </div>
         
        {/* Carte pour créer un tournoi */}
        <div 
          onClick={() => setShowTournamentModal(true)}
          className="bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 cursor-pointer transition duration-150"
        >
          <h2 className="text-xl font-semibold text-indigo-400 mb-3">Tournois</h2>
          <p className="text-gray-400 mb-4">Créer un nouveau tournoi.</p>
          <span className="font-medium text-indigo-400 hover:text-indigo-300">Créer un tournoi &rarr;</span>
        </div>
        
        {/* Carte pour créer un match */}
        <div 
          onClick={() => setShowMatchModal(true)}
          className="bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 cursor-pointer transition duration-150"
        >
          <h2 className="text-xl font-semibold text-indigo-400 mb-3">Matchs</h2>
          <p className="text-gray-400 mb-4">Créer un nouveau match.</p>
          <span className="font-medium text-indigo-400 hover:text-indigo-300">Créer un match &rarr;</span>
        </div>
      </div>

      {/* Modal pour rejoindre une équipe */}
      {showJoinTeamModal && (
        <Modal 
          title="Rejoindre une équipe" 
          onClose={() => setShowJoinTeamModal(false)}
        >
          <div className="space-y-4">
            <p className="text-gray-300">
              Sélectionnez l'équipe que vous souhaitez rejoindre:
            </p>
            
            <select 
              value={selectedTeamId}
              onChange={(e) => setSelectedTeamId(e.target.value)}
              className="w-full"
              required
            >
              <option value="">Sélectionner une équipe</option>
              {teams.map(team => (
                <option key={team.id} value={team.id}>
                  {team.name}
                </option>
              ))}
            </select>

            <div className="flex justify-end space-x-3 pt-4">
              <button 
                type="button" 
                onClick={() => setShowJoinTeamModal(false)} 
                className="btn-secondary"
                disabled={loading}
              >
                Annuler
              </button>
              <button 
                type="button" 
                onClick={handleJoinTeam}
                className="btn-primary"
                disabled={loading}
              >
                {loading ? 'Traitement...' : 'Rejoindre l\'équipe'}
              </button>
            </div>
          </div>
        </Modal>
      )}

      {/* Modal pour créer un tournoi */}
      {showTournamentModal && (
        <Modal 
          title="Créer un tournoi" 
          onClose={() => setShowTournamentModal(false)}
        >
          <TournamentForm 
            onSave={handleSaveTournament} 
            onCancel={() => setShowTournamentModal(false)} 
          />
        </Modal>
      )}

      {/* Modal pour créer un match */}
      {showMatchModal && (
        <Modal 
          title="Créer un match" 
          onClose={() => setShowMatchModal(false)}
        >
          <MatchForm 
            onSave={handleSaveMatch} 
            onCancel={() => setShowMatchModal(false)} 
          />
        </Modal>
      )}
    </div>
  );
};

export default UserDashboard; 