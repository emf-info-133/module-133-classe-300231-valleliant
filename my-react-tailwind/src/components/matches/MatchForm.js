import React, { useState, useEffect } from 'react';
import * as api from '../../services/api';

const MatchForm = ({ match, onSave, onCancel }) => {
  // État pour les champs du formulaire
  const [tournamentId, setTournamentId] = useState('');
  const [team1Id, setTeam1Id] = useState('');
  const [team2Id, setTeam2Id] = useState('');
  const [score1, setScore1] = useState('');
  const [score2, setScore2] = useState('');
  const [matchDate, setMatchDate] = useState('');
  const [status, setStatus] = useState('Planned');
  
  // État pour les listes déroulantes
  const [tournaments, setTournaments] = useState([]);
  const [teams, setTeams] = useState([]);

  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [loadingDropdowns, setLoadingDropdowns] = useState(true);

  // Charger les tournois et équipes pour les listes déroulantes
  useEffect(() => {
    const fetchDropdownData = async () => {
      setLoadingDropdowns(true);
      try {
        const [tournamentsRes, teamsRes] = await Promise.all([
          api.getAllTournaments(),
          api.getAllTeams()
        ]);
        setTournaments(tournamentsRes.data || []);
        setTeams(teamsRes.data || []);
      } catch (err) {
        setError('Erreur chargement des données pour le formulaire (tournois/équipes).');
        console.error(err);
      } finally {
        setLoadingDropdowns(false);
      }
    };
    fetchDropdownData();
  }, []);

  // Pré-remplir le formulaire en mode édition
  useEffect(() => {
    if (match) {
      setTournamentId(match.tournamentId || '');
      setTeam1Id(match.team1Id || '');
      setTeam2Id(match.team2Id || '');
      setScore1(match.score1 ?? ''); // Utiliser ?? pour gérer null/undefined
      setScore2(match.score2 ?? '');
      // Formater la date pour l'input type="datetime-local"
      setMatchDate(match.matchDate ? new Date(match.matchDate).toISOString().slice(0, 16) : '');
      setStatus(match.status || 'Planned');
    } else {
      // Réinitialiser pour création
      setTournamentId('');
      setTeam1Id('');
      setTeam2Id('');
      setScore1('');
      setScore2('');
      setMatchDate('');
      setStatus('Planned');
    }
  }, [match]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (team1Id === team2Id && team1Id !== '') {
        setError('Une équipe ne peut pas jouer contre elle-même.');
        return;
    }
    setError('');
    setLoading(true);

    const matchData = {
      tournamentId: parseInt(tournamentId) || null,
      team1Id: parseInt(team1Id) || null,
      team2Id: parseInt(team2Id) || null,
      score1: score1 === '' ? null : parseInt(score1),
      score2: score2 === '' ? null : parseInt(score2),
      matchDate: matchDate ? new Date(matchDate).toISOString() : null,
      status,
    };

    try {
      let savedMatch;
      if (match) {
        await api.updateMatch(match.id, matchData);
        savedMatch = { ...match, ...matchData };
      } else {
        const response = await api.createMatch(matchData);
        savedMatch = response.data;
      }
      onSave(savedMatch);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la sauvegarde du match.');
      console.error("Erreur sauvegarde match:", err);
    } finally {
      setLoading(false);
    }
  };

  if (loadingDropdowns) {
      return <p className="text-center text-gray-400">Chargement des options...</p>;
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {/* Sélection Tournoi */}
      <div>
        <label htmlFor="match-tournament" className="block text-sm font-medium text-gray-300 mb-1">Tournoi *</label>
        <select 
            id="match-tournament"
            value={tournamentId}
            onChange={(e) => setTournamentId(e.target.value)}
            required
            className="w-full"
        >
            <option value="" disabled>Sélectionner un tournoi</option>
            {tournaments.map(t => (
                <option key={t.id} value={t.id}>{t.name} ({t.game})</option>
            ))}
        </select>
      </div>
      
      {/* Sélection Équipes */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label htmlFor="match-team1" className="block text-sm font-medium text-gray-300 mb-1">Équipe 1 *</label>
            <select 
                id="match-team1"
                value={team1Id}
                onChange={(e) => setTeam1Id(e.target.value)}
                required
                className="w-full"
            >
                <option value="" disabled>Sélectionner l'équipe 1</option>
                 {teams.map(t => (
                    <option key={t.id} value={t.id}>{t.name}</option>
                ))}
            </select>
          </div>
           <div>
            <label htmlFor="match-team2" className="block text-sm font-medium text-gray-300 mb-1">Équipe 2 *</label>
            <select 
                id="match-team2"
                value={team2Id}
                onChange={(e) => setTeam2Id(e.target.value)}
                required
                className="w-full"
            >
                <option value="" disabled>Sélectionner l'équipe 2</option>
                 {teams.map(t => (
                    <option key={t.id} value={t.id}>{t.name}</option>
                ))}
            </select>
          </div>
      </div>

       {/* Scores */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
           <div>
            <label htmlFor="match-score1" className="block text-sm font-medium text-gray-300 mb-1">Score Équipe 1</label>
            <input
              id="match-score1"
              type="number"
              min="0"
              value={score1}
              onChange={(e) => setScore1(e.target.value)}
              placeholder="-"
              className="w-full"
            />
          </div>
           <div>
            <label htmlFor="match-score2" className="block text-sm font-medium text-gray-300 mb-1">Score Équipe 2</label>
            <input
              id="match-score2"
              type="number"
              min="0"
              value={score2}
              onChange={(e) => setScore2(e.target.value)}
              placeholder="-"
              className="w-full"
            />
          </div>
      </div>

      {/* Date et Statut */}
       <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
           <div>
            <label htmlFor="match-date" className="block text-sm font-medium text-gray-300 mb-1">Date et Heure</label>
            <input
              id="match-date"
              type="datetime-local"
              value={matchDate}
              onChange={(e) => setMatchDate(e.target.value)}
              className="w-full"
            />
          </div>
            <div>
                <label htmlFor="match-status" className="block text-sm font-medium text-gray-300 mb-1">Statut</label>
                <select 
                    id="match-status"
                    value={status}
                    onChange={(e) => setStatus(e.target.value)}
                    className="w-full"
                >
                    <option value="Planned">Planifié</option>
                    <option value="Ongoing">En cours</option>
                    <option value="Completed">Terminé</option>
                     <option value="Postponed">Reporté</option>
                    <option value="Cancelled">Annulé</option>
                </select>
            </div>
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
          {loading ? 'Sauvegarde...' : (match ? 'Mettre à jour' : 'Créer le match')}
        </button>
      </div>
    </form>
  );
};

export default MatchForm; 