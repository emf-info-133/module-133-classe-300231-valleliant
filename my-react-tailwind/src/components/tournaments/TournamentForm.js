import React, { useState, useEffect } from 'react';
import * as api from '../../services/api';

const TournamentForm = ({ tournament, onSave, onCancel }) => {
  const [name, setName] = useState('');
  const [game, setGame] = useState('');
  const [startDate, setStartDate] = useState('');
  const [status, setStatus] = useState('Planned'); // Valeur par défaut
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (tournament) {
      setName(tournament.name || '');
      setGame(tournament.game || '');
      // Formater la date pour l'input type="date"
      setStartDate(tournament.startDate ? new Date(tournament.startDate).toISOString().split('T')[0] : '');
      setStatus(tournament.status || 'Planned');
    } else {
      setName('');
      setGame('');
      setStartDate('');
      setStatus('Planned');
    }
  }, [tournament]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    const tournamentData = {
      name,
      game,
      startDate: startDate || null, // Envoyer null si vide
      status,
      // Ajoutez d'autres champs si nécessaire (ex: description, maxTeams)
    };

    try {
      let savedTournament;
      if (tournament) {
        // Mode Édition
        await api.updateTournament(tournament.id, tournamentData);
        savedTournament = { ...tournament, ...tournamentData };
      } else {
        // Mode Création
        const response = await api.createTournament(tournamentData);
        savedTournament = response.data;
      }
      onSave(savedTournament);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la sauvegarde du tournoi.');
      console.error("Erreur sauvegarde tournoi:", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label htmlFor="tournament-name" className="block text-sm font-medium text-gray-300 mb-1">Nom du tournoi</label>
        <input
          id="tournament-name"
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
          placeholder="Ex : Championnat d'été"
          className="w-full"
        />
      </div>
      <div>
        <label htmlFor="tournament-game" className="block text-sm font-medium text-gray-300 mb-1">Jeu</label>
        <input
          id="tournament-game"
          type="text"
          value={game}
          onChange={(e) => setGame(e.target.value)}
          required
          placeholder="Ex : Counter-Strike 2"
          className="w-full"
        />
      </div>
       <div>
        <label htmlFor="tournament-startdate" className="block text-sm font-medium text-gray-300 mb-1">Date de début</label>
        <input
          id="tournament-startdate"
          type="date"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
          className="w-full"
        />
      </div>
       <div>
        <label htmlFor="tournament-status" className="block text-sm font-medium text-gray-300 mb-1">Statut</label>
        <select 
            id="tournament-status"
            value={status}
            onChange={(e) => setStatus(e.target.value)}
            className="w-full"
        >
            <option value="Planned">Planifié</option>
            <option value="RegistrationOpen">Inscriptions Ouvertes</option>
            <option value="RegistrationClosed">Inscriptions Fermées</option>
            <option value="Ongoing">En cours</option>
            <option value="Completed">Terminé</option>
            <option value="Cancelled">Annulé</option>
        </select>
      </div>
       {/* Ajoutez d'autres champs ici (description, etc.) */}

      {error && (
        <div className="text-red-500 text-sm text-center p-3 bg-red-900 border border-red-700 rounded">
            {error}
        </div>
      )}

      <div className="flex justify-end space-x-3 pt-4">
        <button type="button" onClick={onCancel} className="btn-secondary" disabled={loading}>
          Annuler
        </button>
        <button type="submit" className="btn-primary" disabled={loading}>
          {loading ? 'Sauvegarde...' : (tournament ? 'Mettre à jour' : 'Créer le tournoi')}
        </button>
      </div>
    </form>
  );
};

export default TournamentForm; 