import React, { useState, useEffect } from 'react';
import * as api from '../../services/api';
import { useAuth } from '../../hooks/useAuth';

const TournamentForm = ({ tournament, onSave, onCancel }) => {
  const { user } = useAuth();
  const [name, setName] = useState('');
  const [date, setDate] = useState('');
  const [gameId, setGameId] = useState('');
  const [games, setGames] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Récupérer la liste des jeux disponibles
  useEffect(() => {
    const fetchGames = async () => {
      try {
        const response = await api.getAllGames();
        setGames(response.data || []);
      } catch (err) {
        console.error("Erreur lors de la récupération des jeux:", err);
      }
    };
    fetchGames();
  }, []);

  useEffect(() => {
    if (tournament) {
      setName(tournament.name || '');
      setDate(tournament.date || '');
      setGameId(tournament.gameId || '');
    } else {
      setName('');
      setDate('');
      setGameId('');
    }
  }, [tournament]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    if (!gameId) {
      setError('Veuillez sélectionner un jeu.');
      setLoading(false);
      return;
    }

    const tournamentData = {
      name,
      date,
      adminId: user?.id,
      gameId: parseInt(gameId, 10)
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
        <label htmlFor="tournament-date" className="block text-sm font-medium text-gray-300 mb-1">Date du tournoi</label>
        <input
          id="tournament-date"
          type="date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
          required
          className="w-full"
        />
      </div>
      <div>
        <label htmlFor="tournament-game" className="block text-sm font-medium text-gray-300 mb-1">Jeu</label>
        <select
          id="tournament-game"
          value={gameId}
          onChange={(e) => setGameId(e.target.value)}
          required
          className="w-full"
        >
          <option value="">Sélectionnez un jeu</option>
          {games.map(game => (
            <option key={game.id} value={game.id}>
              {game.name}
            </option>
          ))}
        </select>
      </div>

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