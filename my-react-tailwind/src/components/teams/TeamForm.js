import React, { useState, useEffect } from 'react';
import * as api from '../../services/api';
import { useAuth } from '../../hooks/useAuth';

const TeamForm = ({ team, onSave, onCancel }) => {
  const { user } = useAuth();
  const [name, setName] = useState('');
  const [tournamentId, setTournamentId] = useState('');
  const [tournaments, setTournaments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // Récupérer la liste des tournois disponibles
  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        const response = await api.getAllTournaments();
        setTournaments(response.data || []);
      } catch (err) {
        console.error("Erreur lors de la récupération des tournois:", err);
      }
    };
    fetchTournaments();
  }, []);

  // Pré-remplir le formulaire si une équipe est fournie (mode édition)
  useEffect(() => {
    if (team) {
      setName(team.name || '');
      setTournamentId(team.tournament || '');
    } else {
      // Réinitialiser pour le mode création
      setName('');
      setTournamentId('');
    }
  }, [team]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    // Validation du tournoi
    if (!tournamentId) {
      setError('Veuillez sélectionner un tournoi.');
      setLoading(false);
      return;
    }

    // Données de l'équipe à envoyer
    const teamData = {
      name,
      tournament: parseInt(tournamentId, 10),
    };

    // Si on est en mode création et que l'API attend l'ID du capitaine
    if (!team && user?.id) {
        teamData.captain = user.id; 
    }

    try {
      let savedTeam;
      if (team) {
        // Mode Édition
        const updateData = { 
          name, 
          tournament: parseInt(tournamentId, 10),
          captain: team.captain
        };
        await api.updateTeam(team.id, updateData);
        savedTeam = { ...team, ...updateData };
      } else {
        // Mode Création
        const response = await api.createTeam(teamData);
        savedTeam = response.data;
      }
      onSave(savedTeam);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la sauvegarde de l\'équipe.');
      console.error("Erreur sauvegarde équipe:", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label htmlFor="team-name" className="block text-sm font-medium text-gray-300 mb-1">Nom de l'équipe</label>
        <input
          id="team-name"
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
          placeholder="Ex : Les Aigles Rouges"
          className="w-full"
        />
      </div>
      
      <div>
        <label htmlFor="team-tournament" className="block text-sm font-medium text-gray-300 mb-1">Tournoi</label>
        <select
          id="team-tournament"
          value={tournamentId}
          onChange={(e) => setTournamentId(e.target.value)}
          required
          className="w-full"
        >
          <option value="">Sélectionnez un tournoi</option>
          {tournaments.map(tournament => (
            <option key={tournament.id} value={tournament.id}>
              {tournament.name}
            </option>
          ))}
        </select>
        <p className="text-xs text-gray-400 mt-1">L'équipe doit être associée à un tournoi.</p>
      </div>

       {/* Afficher le capitaine (non modifiable ici) */}
       {team && (
           <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Capitaine (ID)</label>
                <p className="text-gray-400 bg-gray-700 px-3 py-2 rounded-md">{team.captain} (non modifiable)</p>
           </div>
       )}
        {!team && (
           <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Capitaine</label>
                <p className="text-gray-400 bg-gray-700 px-3 py-2 rounded-md">{user?.name || user?.email} (Vous)</p>
           </div>
       )}

      {error && (
        <div className="text-red-500 text-sm text-center p-3 bg-red-900 border border-red-700 rounded">
            {error}
        </div>
      )}

      <div className="flex justify-end space-x-3 pt-4">
        <button 
          type="button" 
          onClick={onCancel} 
          className="btn-secondary"
          disabled={loading}
        >
          Annuler
        </button>
        <button 
          type="submit" 
          className="btn-primary"
          disabled={loading}
        >
          {loading ? 'Sauvegarde...' : (team ? 'Mettre à jour' : 'Créer l\'équipe')}
        </button>
      </div>
    </form>
  );
};

export default TeamForm; 