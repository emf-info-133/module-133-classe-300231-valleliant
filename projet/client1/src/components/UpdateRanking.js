import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getRankingByTournament, updateTeamRanking } from '../services/rankingService';
import { getTeamsByTournament } from '../services/teamService';
import './UpdateRanking.css';

const UpdateRanking = () => {
  const { tournamentId } = useParams();
  const [ranking, setRanking] = useState(null);
  const [teams, setTeams] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [updateLoading, setUpdateLoading] = useState(false);
  const [selectedTeam, setSelectedTeam] = useState('');
  const [updateData, setUpdateData] = useState({
    points: 0,
    matchesPlayed: 0,
    wins: 0,
    draws: 0,
    losses: 0
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        // Récupérer le classement actuel
        const rankingData = await getRankingByTournament(tournamentId);
        setRanking(rankingData);
        
        // Récupérer toutes les équipes du tournoi
        const teamsData = await getTeamsByTournament(tournamentId);
        setTeams(teamsData);
        
        setError(null);
      } catch (err) {
        setError('Erreur lors du chargement des données: ' + err.message);
      } finally {
        setLoading(false);
      }
    };

    if (tournamentId) {
      fetchData();
    }
  }, [tournamentId]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUpdateData({
      ...updateData,
      [name]: parseInt(value, 10) || 0
    });
  };

  const handleTeamSelect = (e) => {
    setSelectedTeam(e.target.value);
    
    // Si l'équipe est déjà dans le classement, préremplir les données
    if (ranking && ranking.teams) {
      const existingTeam = ranking.teams.find(t => t.teamId === parseInt(e.target.value, 10));
      if (existingTeam) {
        setUpdateData({
          points: existingTeam.points,
          matchesPlayed: existingTeam.matchesPlayed,
          wins: existingTeam.wins,
          draws: existingTeam.draws,
          losses: existingTeam.losses
        });
      } else {
        // Réinitialiser les valeurs si l'équipe n'est pas encore classée
        setUpdateData({
          points: 0,
          matchesPlayed: 0,
          wins: 0, 
          draws: 0,
          losses: 0
        });
      }
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!selectedTeam) {
      setError('Veuillez sélectionner une équipe');
      return;
    }
    
    try {
      setUpdateLoading(true);
      setError(null);
      
      await updateTeamRanking(ranking.id, selectedTeam, updateData);
      
      // Rafraîchir les données
      const updatedRanking = await getRankingByTournament(tournamentId);
      setRanking(updatedRanking);
      
      setSuccess('Classement mis à jour avec succès');
      
      // Effacer le message de succès après 3 secondes
      setTimeout(() => setSuccess(null), 3000);
    } catch (err) {
      setError('Erreur lors de la mise à jour du classement: ' + err.message);
    } finally {
      setUpdateLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Chargement des données...</div>;
  }

  if (!ranking) {
    return <div className="error-message">Aucun classement trouvé pour ce tournoi</div>;
  }

  return (
    <div className="update-ranking">
      <h2>Mettre à jour le classement: {ranking.name}</h2>
      
      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}
      
      <form onSubmit={handleSubmit} className="ranking-form">
        <div className="form-group">
          <label htmlFor="team-select">Sélectionner une équipe</label>
          <select 
            id="team-select"
            value={selectedTeam}
            onChange={handleTeamSelect}
            className="form-control"
            required
          >
            <option value="">-- Choisir une équipe --</option>
            {teams.map(team => (
              <option key={team.id} value={team.id}>
                {team.name}
              </option>
            ))}
          </select>
        </div>
        
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="matchesPlayed">Matchs joués</label>
            <input
              type="number"
              id="matchesPlayed"
              name="matchesPlayed"
              value={updateData.matchesPlayed}
              onChange={handleInputChange}
              min="0"
              className="form-control"
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="points">Points</label>
            <input
              type="number"
              id="points"
              name="points"
              value={updateData.points}
              onChange={handleInputChange}
              min="0"
              className="form-control"
            />
          </div>
        </div>
        
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="wins">Victoires</label>
            <input
              type="number"
              id="wins"
              name="wins"
              value={updateData.wins}
              onChange={handleInputChange}
              min="0"
              className="form-control"
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="draws">Matchs nuls</label>
            <input
              type="number"
              id="draws"
              name="draws"
              value={updateData.draws}
              onChange={handleInputChange}
              min="0"
              className="form-control"
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="losses">Défaites</label>
            <input
              type="number"
              id="losses"
              name="losses"
              value={updateData.losses}
              onChange={handleInputChange}
              min="0"
              className="form-control"
            />
          </div>
        </div>
        
        <button 
          type="submit" 
          className="submit-button"
          disabled={updateLoading}
        >
          {updateLoading ? 'Mise à jour...' : 'Mettre à jour le classement'}
        </button>
      </form>
      
      <div className="current-ranking">
        <h3>Classement actuel</h3>
        {ranking.teamsWithDetails && ranking.teamsWithDetails.length > 0 ? (
          <table className="ranking-table">
            <thead>
              <tr>
                <th>Position</th>
                <th>Équipe</th>
                <th>Points</th>
                <th>J</th>
                <th>V</th>
                <th>N</th>
                <th>D</th>
              </tr>
            </thead>
            <tbody>
              {ranking.teamsWithDetails.map((teamRanking) => (
                <tr key={teamRanking.teamId}>
                  <td>{teamRanking.position}</td>
                  <td>{teamRanking.team ? teamRanking.team.name : `Équipe #${teamRanking.teamId}`}</td>
                  <td>{teamRanking.points}</td>
                  <td>{teamRanking.matchesPlayed}</td>
                  <td>{teamRanking.wins}</td>
                  <td>{teamRanking.draws}</td>
                  <td>{teamRanking.losses}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p>Aucune équipe classée pour le moment.</p>
        )}
      </div>
    </div>
  );
};

export default UpdateRanking; 