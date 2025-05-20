import React, { useState, useEffect } from 'react';
import * as api from '../../services/api';

const MatchForm = ({ match, onSave, onCancel }) => {
  // État pour les champs du formulaire
  const [tournamentId, setTournamentId] = useState('');
  const [team1Id, setTeam1Id] = useState('');
  const [team2Id, setTeam2Id] = useState('');
  const [score1, setScore1] = useState('');
  const [score2, setScore2] = useState('');
  const [date, setDate] = useState('');
  
  // État pour les listes déroulantes
  const [tournaments, setTournaments] = useState([]);
  const [teams, setTeams] = useState([]);
  const [filteredTeams, setFilteredTeams] = useState([]);

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

  // Filtrer les équipes en fonction du tournoi sélectionné
  useEffect(() => {
    if (tournamentId) {
      const tournamentTeams = teams.filter(team => team.tournament === parseInt(tournamentId, 10));
      setFilteredTeams(tournamentTeams);
    } else {
      setFilteredTeams([]);
    }
  }, [tournamentId, teams]);

  // Pré-remplir le formulaire en mode édition
  useEffect(() => {
    if (match) {
      setTournamentId(match.tournamentId?.toString() || '');
      setTeam1Id(match.team1Id?.toString() || '');
      setTeam2Id(match.team2Id?.toString() || '');
      setScore1(match.score1 || '');
      setScore2(match.score2 || '');
      setDate(match.date || '');
    } else {
      // Réinitialiser pour création
      setTournamentId('');
      setTeam1Id('');
      setTeam2Id('');
      setScore1('');
      setScore2('');
      setDate('');
    }
  }, [match]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validation des champs requis
    if (!tournamentId) {
      setError('Veuillez sélectionner un tournoi.');
      return;
    }
    
    if (!team1Id) {
      setError('Veuillez sélectionner la première équipe.');
      return;
    }
    
    if (!team2Id) {
      setError('Veuillez sélectionner la deuxième équipe.');
      return;
    }
    
    if (!date) {
      setError('La date du match est requise.');
      return;
    }
    
    if (team1Id === team2Id) {
      setError('Une équipe ne peut pas jouer contre elle-même.');
      return;
    }
    
    setError('');
    setLoading(true);

    try {
      // Conversion explicite des IDs en nombres
      const tournamentIdNum = parseInt(tournamentId, 10);
      const team1IdNum = parseInt(team1Id, 10);
      const team2IdNum = parseInt(team2Id, 10);
      
      // Vérification des valeurs numériques
      if (isNaN(tournamentIdNum) || tournamentIdNum > 2147483647) {
        throw new Error('ID tournoi invalide');
      }

      if (isNaN(team1IdNum) || team1IdNum > 2147483647) {
        throw new Error('ID équipe 1 invalide');
      }

      if (isNaN(team2IdNum) || team2IdNum > 2147483647) {
        throw new Error('ID équipe 2 invalide');
      }

      // Format exact attendu par l'API selon MatchDTO
      const matchData = {
        tournamentId: tournamentIdNum,
        team1Id: team1IdNum,
        team2Id: team2IdNum,
        score1,
        score2,
        date
      };

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
      if (err.message) {
        setError(err.message);
      } else {
        setError(err.response?.data?.message || 'Erreur lors de la sauvegarde du match.');
      }
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
          onChange={(e) => {
            setTournamentId(e.target.value);
            setTeam1Id('');
            setTeam2Id('');
          }}
          required
          className="w-full"
        >
          <option value="">Sélectionner un tournoi</option>
          {tournaments.map(t => (
            <option key={t.id} value={t.id}>{t.name}</option>
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
            disabled={!tournamentId}
            className="w-full"
          >
            <option value="">Sélectionner l'équipe 1</option>
            {filteredTeams.map(t => (
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
            disabled={!tournamentId}
            className="w-full"
          >
            <option value="">Sélectionner l'équipe 2</option>
            {filteredTeams.map(t => (
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
            type="text"
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
            type="text"
            value={score2}
            onChange={(e) => setScore2(e.target.value)}
            placeholder="-"
            className="w-full"
          />
        </div>
      </div>

      {/* Date */}
      <div>
        <label htmlFor="match-date" className="block text-sm font-medium text-gray-300 mb-1">Date du match</label>
        <input
          id="match-date"
          type="date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
          required
          className="w-full"
        />
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