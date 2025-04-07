import React, { useState } from 'react';
import { createTeam } from '../../services/tournamentService';

const TeamCreationModal = ({ tournament, onClose }) => {
  const [teamName, setTeamName] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      await createTeam(tournament.id, teamName);
      setSuccess(true);
      setTimeout(() => {
        onClose();
      }, 2000);
    } catch (err) {
      setError(err.message || 'Échec de la création de l\'équipe');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-8 max-w-md w-full">
        <h2 className="text-2xl font-bold mb-4">
          Créer une équipe pour {tournament.name}
        </h2>
        
        {success ? (
          <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
            Équipe créée avec succès ! Redirection...
          </div>
        ) : (
          <form onSubmit={handleSubmit}>
            {error && (
              <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                {error}
              </div>
            )}
            
            <div className="mb-4">
              <label className="block text-gray-700 mb-2" htmlFor="teamName">
                Nom de l'équipe
              </label>
              <input
                id="teamName"
                type="text"
                className="w-full p-2 border rounded"
                value={teamName}
                onChange={(e) => setTeamName(e.target.value)}
                required
              />
            </div>
            
            <div className="flex justify-end space-x-2">
              <button
                type="button"
                className="px-4 py-2 border rounded hover:bg-gray-100 transition"
                onClick={onClose}
                disabled={loading}
              >
                Annuler
              </button>
              <button
                type="submit"
                className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
                disabled={loading}
              >
                {loading ? 'Création...' : 'Créer l\'équipe'}
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
};

export default TeamCreationModal; 