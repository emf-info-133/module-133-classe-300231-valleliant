import React, { useState, useEffect, useContext } from 'react';
import { AdminContext } from '../contexts/AdminContext';
import { Link } from 'react-router-dom';
import AdminLayout from '../components/Layout/AdminLayout';
import { getTournamentsByAdmin } from '../services/tournamentService';
import { getAllTeams } from '../services/teamService';
import { getAllMatches } from '../services/matchService';
import '../styles/Dashboard.css';

const Dashboard = () => {
  const { admin } = useContext(AdminContext);
  const [tournaments, setTournaments] = useState([]);
  const [teams, setTeams] = useState([]);
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        // Récupérer les tournois de l'administrateur connecté
        const adminId = admin?.id || 1;
        const tournamentsData = await getTournamentsByAdmin(adminId);
        setTournaments(tournamentsData);
        
        // Récupérer toutes les équipes
        const teamsData = await getAllTeams();
        setTeams(teamsData);
        
        // Récupérer tous les matchs
        const matchesData = await getAllMatches();
        setMatches(matchesData);
        
        setError(null);
      } catch (err) {
        setError('Erreur lors du chargement des données');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [admin]);

  // Récupérer les matchs à venir (prochains 7 jours)
  const getUpcomingMatches = () => {
    const now = new Date();
    const nextWeek = new Date();
    nextWeek.setDate(now.getDate() + 7);
    
    return matches
      .filter(match => {
        const matchDate = new Date(match.date);
        return matchDate >= now && matchDate <= nextWeek && match.status === 'scheduled';
      })
      .sort((a, b) => new Date(a.date) - new Date(b.date))
      .slice(0, 5); // Limiter à 5 matchs
  };

  // Formater une date
  const formatDate = (dateString) => {
    const options = { 
      year: 'numeric', 
      month: 'short', 
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    };
    return new Date(dateString).toLocaleDateString('fr-FR', options);
  };

  // Compter les tournois par statut
  const countTournamentsByStatus = () => {
    const counts = {
      active: 0,
      upcoming: 0,
      completed: 0
    };
    
    tournaments.forEach(tournament => {
      if (counts[tournament.status] !== undefined) {
        counts[tournament.status]++;
      }
    });
    
    return counts;
  };

  if (loading) {
    return (
      <AdminLayout>
        <div className="loading-indicator">Chargement des données...</div>
      </AdminLayout>
    );
  }

  const upcomingMatches = getUpcomingMatches();
  const tournamentCounts = countTournamentsByStatus();

  return (
    <AdminLayout>
      {error && <div className="error-message">{error}</div>}
      
      <div className="dashboard-welcome">
        <h2>Bienvenue, {admin?.name || 'Administrateur'}</h2>
        <p>Consultez les statistiques et les événements à venir.</p>
      </div>
      
      <div className="dashboard-stats">
        <div className="stat-card">
          <div className="stat-icon tournaments-icon">
            <i className="fas fa-trophy"></i>
          </div>
          <div className="stat-content">
            <h3>Tournois</h3>
            <p className="stat-number">{tournaments.length}</p>
            <div className="stat-details">
              <span>{tournamentCounts.active} actifs</span>
              <span>{tournamentCounts.upcoming} à venir</span>
              <span>{tournamentCounts.completed} terminés</span>
            </div>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon teams-icon">
            <i className="fas fa-users"></i>
          </div>
          <div className="stat-content">
            <h3>Équipes</h3>
            <p className="stat-number">{teams.length}</p>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon matches-icon">
            <i className="fas fa-gamepad"></i>
          </div>
          <div className="stat-content">
            <h3>Matchs</h3>
            <p className="stat-number">{matches.length}</p>
            <div className="stat-details">
              <span>{matches.filter(m => m.status === 'completed').length} joués</span>
              <span>{matches.filter(m => m.status === 'scheduled').length} programmés</span>
            </div>
          </div>
        </div>
      </div>
      
      <div className="dashboard-sections">
        <div className="dashboard-section">
          <div className="section-header">
            <h3>Matchs à venir</h3>
            <Link to="/matches" className="view-all-link">Voir tous</Link>
          </div>
          
          {upcomingMatches.length > 0 ? (
            <div className="upcoming-matches">
              {upcomingMatches.map(match => {
                const teamA = teams.find(t => t.id === match.teamAId);
                const teamB = teams.find(t => t.id === match.teamBId);
                const tournament = tournaments.find(t => t.id === match.tournamentId);
                
                return (
                  <div key={match.id} className="match-card">
                    <div className="match-info">
                      <div className="match-teams">
                        <span className="team-name">{teamA ? teamA.name : 'Équipe A'}</span>
                        <span className="vs">VS</span>
                        <span className="team-name">{teamB ? teamB.name : 'Équipe B'}</span>
                      </div>
                      <div className="match-date">{formatDate(match.date)}</div>
                      <div className="match-location">{match.location}</div>
                      {tournament && <div className="match-tournament">{tournament.name}</div>}
                    </div>
                    <Link to={`/match/${match.id}`} className="view-match-link">
                      <i className="fas fa-arrow-right"></i>
                    </Link>
                  </div>
                );
              })}
            </div>
          ) : (
            <p className="no-data-message">Aucun match programmé dans les 7 prochains jours.</p>
          )}
        </div>
        
        <div className="dashboard-section">
          <div className="section-header">
            <h3>Tournois récents</h3>
            <Link to="/tournaments" className="view-all-link">Voir tous</Link>
          </div>
          
          {tournaments.length > 0 ? (
            <div className="recent-tournaments">
              {tournaments.slice(0, 3).map(tournament => (
                <div key={tournament.id} className="tournament-card">
                  <div className="tournament-info">
                    <h4>{tournament.name}</h4>
                    <div className="tournament-dates">
                      {new Date(tournament.startDate).toLocaleDateString('fr-FR')} - {new Date(tournament.endDate).toLocaleDateString('fr-FR')}
                    </div>
                    <div className={`tournament-status status-${tournament.status}`}>
                      {tournament.status === 'active' && 'Actif'}
                      {tournament.status === 'upcoming' && 'À venir'}
                      {tournament.status === 'completed' && 'Terminé'}
                    </div>
                  </div>
                  <Link to={`/tournament/${tournament.id}`} className="view-tournament-link">
                    <i className="fas fa-arrow-right"></i>
                  </Link>
                </div>
              ))}
            </div>
          ) : (
            <p className="no-data-message">Aucun tournoi trouvé.</p>
          )}
        </div>
      </div>
      
      <div className="dashboard-actions">
        <Link to="/tournament/new" className="action-button create-tournament">
          <i className="fas fa-plus"></i> Créer un tournoi
        </Link>
        <Link to="/match/new" className="action-button create-match">
          <i className="fas fa-plus"></i> Programmer un match
        </Link>
        <Link to="/team/new" className="action-button create-team">
          <i className="fas fa-plus"></i> Créer une équipe
        </Link>
      </div>
    </AdminLayout>
  );
};

export default Dashboard; 