import React, { useState, useEffect, useContext } from 'react';
import { Link } from 'react-router-dom';
import { AdminContext } from '../../contexts/AdminContext';
import AdminLayout from '../../components/Layout/AdminLayout';
import { getTournamentsByAdmin, deleteTournament } from '../../services/tournamentService';
import { getGameById } from '../../services/gameService';
import '../../styles/TournamentList.css';

const TournamentList = () => {
  const [tournaments, setTournaments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [deleteConfirm, setDeleteConfirm] = useState(null);
  const [gamesCache, setGamesCache] = useState({});
  
  const { admin } = useContext(AdminContext);

  // Récupérer les tournois
  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        setLoading(true);
        const adminId = admin?.id || 1;
        const data = await getTournamentsByAdmin(adminId);
        setTournaments(data);
        setError(null);
      } catch (err) {
        setError('Erreur lors de la récupération des tournois');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchTournaments();
  }, [admin]);

  // Récupérer les détails d'un jeu pour un tournoi
  const fetchGameDetails = async (gameId) => {
    // Vérifier si les détails du jeu sont déjà dans le cache
    if (gamesCache[gameId]) {
      return gamesCache[gameId];
    }
    
    try {
      const game = await getGameById(gameId);
      // Mettre à jour le cache
      setGamesCache(prev => ({
        ...prev,
        [gameId]: game
      }));
      return game;
    } catch (error) {
      console.error('Erreur lors de la récupération des détails du jeu:', error);
      return null;
    }
  };

  // Charger les détails des jeux pour les tournois
  useEffect(() => {
    const loadGamesForTournaments = async () => {
      if (tournaments.length === 0) return;
      
      const gameIds = [...new Set(tournaments.map(t => t.gameId))];
      
      for (const gameId of gameIds) {
        if (!gamesCache[gameId]) {
          await fetchGameDetails(gameId);
        }
      }
    };
    
    loadGamesForTournaments();
  }, [tournaments, gamesCache]);

  // Supprimer un tournoi
  const handleDeleteTournament = async (id) => {
    try {
      await deleteTournament(id);
      // Mettre à jour la liste des tournois après suppression
      setTournaments(prev => prev.filter(tournament => tournament.id !== id));
      setDeleteConfirm(null);
    } catch (err) {
      setError('Erreur lors de la suppression du tournoi');
      console.error(err);
    }
  };

  // Formater une date
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('fr-FR');
  };

  // Rendu conditionnel pour l'état de chargement
  if (loading) {
    return (
      <AdminLayout>
        <div className="loading-indicator">Chargement des tournois...</div>
      </AdminLayout>
    );
  }

  return (
    <AdminLayout>
      <div className="tournament-list-container">
        <div className="tournament-list-header">
          <div className="tournament-count">
            {tournaments.length} tournoi{tournaments.length !== 1 ? 's' : ''} trouvé{tournaments.length !== 1 ? 's' : ''}
          </div>
          <Link to="/tournament/new" className="btn-create-tournament">
            <i className="fas fa-plus"></i> Nouveau tournoi
          </Link>
        </div>
        
        {error && <div className="error-message">{error}</div>}
        
        {deleteConfirm && (
          <div className="delete-confirmation">
            <p>Êtes-vous sûr de vouloir supprimer le tournoi "{deleteConfirm.name}" ?</p>
            <div className="confirmation-actions">
              <button className="btn-cancel" onClick={() => setDeleteConfirm(null)}>Annuler</button>
              <button className="btn-delete" onClick={() => handleDeleteTournament(deleteConfirm.id)}>Supprimer</button>
            </div>
          </div>
        )}
        
        {tournaments.length === 0 ? (
          <div className="empty-list">
            <p>Aucun tournoi trouvé. Commencez par en créer un.</p>
          </div>
        ) : (
          <div className="tournament-list">
            {tournaments.map(tournament => {
              const gameInfo = gamesCache[tournament.gameId];
              
              return (
                <div key={tournament.id} className="tournament-card">
                  <div className="tournament-main-info">
                    <h3>{tournament.name}</h3>
                    <div className={`tournament-status status-${tournament.status}`}>
                      {tournament.status === 'active' && 'Actif'}
                      {tournament.status === 'upcoming' && 'À venir'}
                      {tournament.status === 'completed' && 'Terminé'}
                    </div>
                  </div>
                  
                  <div className="tournament-details">
                    <div className="detail-item">
                      <span className="detail-label">Jeu:</span>
                      <span className="detail-value">{gameInfo ? gameInfo.name : 'Chargement...'}</span>
                    </div>
                    <div className="detail-item">
                      <span className="detail-label">Période:</span>
                      <span className="detail-value">
                        {formatDate(tournament.startDate)} - {formatDate(tournament.endDate)}
                      </span>
                    </div>
                  </div>
                  
                  <div className="tournament-description">
                    {tournament.description.length > 150 
                      ? tournament.description.substring(0, 150) + '...' 
                      : tournament.description}
                  </div>
                  
                  <div className="tournament-actions">
                    <Link to={`/tournament/${tournament.id}`} className="btn-view">
                      <i className="fas fa-eye"></i> Voir
                    </Link>
                    <Link to={`/tournament/edit/${tournament.id}`} className="btn-edit">
                      <i className="fas fa-edit"></i> Modifier
                    </Link>
                    <button 
                      className="btn-delete"
                      onClick={() => setDeleteConfirm(tournament)}
                    >
                      <i className="fas fa-trash"></i> Supprimer
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </AdminLayout>
  );
};

export default TournamentList; 