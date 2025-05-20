import React, { useState, useEffect } from 'react';
import { useAuth } from '../hooks/useAuth';
import { Link } from 'react-router-dom';
import * as api from '../services/api';
import Modal from '../components/common/Modal';

const UserDashboard = () => {
  const { user } = useAuth();
  const [showJoinTeamModal, setShowJoinTeamModal] = useState(false);
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

    setLoading(true);
    setError('');
    setSuccess('');

    try {
      const teamUserData = {
        userId: user?.id,
        teamId: parseInt(selectedTeamId, 10)
      };
      
      await api.joinTeam(teamUserData);
      setSuccess(`Vous avez rejoint l'équipe avec succès !`);
      setShowJoinTeamModal(false);
    } catch (err) {
      setError(err.response?.data?.message || "Erreur lors de la tentative de rejoindre l'équipe.");
    } finally {
      setLoading(false);
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
         
        {/* Carte pour voir les tournois */}
        <div className="bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 transition duration-150">
          <h2 className="text-xl font-semibold text-indigo-400 mb-3">Tournois</h2>
          <p className="text-gray-400 mb-4">Consulter les tournois disponibles et passés.</p>
          <span className="text-gray-500">(Fonctionnalité à venir)</span>
        </div>
        
        {/* Carte pour voir les matchs */}
        <div className="bg-gray-800 p-6 rounded-lg shadow-lg hover:bg-gray-700 transition duration-150">
          <h2 className="text-xl font-semibold text-indigo-400 mb-3">Matchs</h2>
          <p className="text-gray-400 mb-4">Suivre les résultats des matchs en cours.</p>
          <span className="text-gray-500">(Fonctionnalité à venir)</span>
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

            {error && (
              <div className="text-red-500 text-sm p-3 bg-red-900 border border-red-700 rounded">
                {error}
              </div>
            )}

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
    </div>
  );
};

export default UserDashboard; 