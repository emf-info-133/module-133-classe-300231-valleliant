import React, { useEffect, useState, useContext } from 'react';
import { getRankingByTournament } from '../services/rankingService';
import { useParams, Link } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';
import './TournamentRanking.css';

const TournamentRanking = () => {
  const [ranking, setRanking] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { tournamentId } = useParams();
  const { user } = useContext(AuthContext);

  useEffect(() => {
    const fetchRanking = async () => {
      try {
        setLoading(true);
        const data = await getRankingByTournament(tournamentId);
        setRanking(data);
        setError(null);
      } catch (err) {
        setError('Impossible de charger le classement: ' + err.message);
      } finally {
        setLoading(false);
      }
    };

    if (tournamentId) {
      fetchRanking();
    }
  }, [tournamentId]);

  if (loading) {
    return <div className="loading">Chargement du classement...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  if (!ranking || !ranking.teamsWithDetails) {
    return <div className="empty-ranking">Aucun classement disponible pour ce tournoi.</div>;
  }

  return (
    <div className="tournament-ranking">
      <div className="ranking-header">
        <h2>{ranking.name}</h2>
        <div className="ranking-actions">
          <Link to={`/tournaments/${tournamentId}`} className="back-link">
            Retour au tournoi
          </Link>
          {user && user.role === 'admin' && (
            <Link to={`/tournaments/${tournamentId}/update-ranking`} className="update-link">
              Mettre à jour le classement
            </Link>
          )}
        </div>
      </div>
      
      {ranking.teamsWithDetails.length === 0 ? (
        <p>Aucune équipe classée pour le moment.</p>
      ) : (
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
      )}
    </div>
  );
};

export default TournamentRanking; 