import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AdminLayout from '../../components/Layout/AdminLayout';
import { getAllTournaments, deleteTournament } from '../../services/tournamentService';
import '../../styles/TournamentList.css';

const TournamentList = () => {
  const [tournaments, setTournaments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [deleteId, setDeleteId] = useState(null);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        setLoading(true);
        const data = await getAllTournaments();
        setTournaments(data);
      } catch (err) {
        setError('Erreur lors du chargement des tournois');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchTournaments();
  }, []);

  const handleDelete = async () => {
    if (!deleteId) return;
    
    try {
      await deleteTournament(deleteId);
      setTournaments(tournaments.filter(t => t.id !== deleteId));
      closeConfirmModal();
    } catch (err) {
      console.error('Erreur lors de la suppression du tournoi:', err);
      setError('Erreur lors de la suppression du tournoi');
    }
  };

  const openConfirmModal = (id) => {
    setDeleteId(id);
    setShowConfirmModal(true);
  };

  const closeConfirmModal = () => {
    setShowConfirmModal(false);
    setDeleteId(null);
  };

  const handleAdd = () => {
    navigate('/tournaments/create');
  };

  const handleEdit = (id) => {
    navigate(`/tournaments/edit/${id}`);
  };

  // Helper pour formater la date
  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('fr-FR', options);
  };

  // Helper pour obtenir le label du statut
  const getStatusLabel = (status) => {
    const statusMap = {
      'upcoming': { label: 'À venir', class: 'status-upcoming' },
      'in_progress': { label: 'En cours', class: 'status-active' },
      'completed': { label: 'Terminé', class: 'status-completed' },
      'cancelled': { label: 'Annulé', class: 'status-cancelled' }
    };

    return statusMap[status] || { label: status, class: '' };
  };

  if (loading) {
    return (
      <AdminLayout>
        <div className="loading-indicator">Chargement des tournois...</div>
      </AdminLayout>
    );
  }

  if (error) {
    return (
      <AdminLayout>
        <div className="error-message">{error}</div>
      </AdminLayout>
    );
  }

  return (
    <AdminLayout>
      <div className="tournament-list-container">
        <div className="tournament-list-header">
          <h2>Liste des tournois</h2>
          <button 
            className="btn-add" 
            onClick={handleAdd}
          >
            Ajouter un tournoi
          </button>
        </div>

        {tournaments.length === 0 ? (
          <div className="empty-state">
            <p>Aucun tournoi n'a été trouvé.</p>
            <button 
              className="btn-primary" 
              onClick={handleAdd}
            >
              Créer votre premier tournoi
            </button>
          </div>
        ) : (
          <div className="table-container">
            <table className="tournament-table">
              <thead>
                <tr>
                  <th>Nom</th>
                  <th>Jeu</th>
                  <th>Date de début</th>
                  <th>Date de fin</th>
                  <th>Statut</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {tournaments.map(tournament => {
                  const statusInfo = getStatusLabel(tournament.status);
                  
                  return (
                    <tr key={tournament.id}>
                      <td>{tournament.name}</td>
                      <td>{tournament.gameType}</td>
                      <td>{formatDate(tournament.startDate)}</td>
                      <td>{formatDate(tournament.endDate)}</td>
                      <td>
                        <span className={`status-badge ${statusInfo.class}`}>
                          {statusInfo.label}
                        </span>
                      </td>
                      <td className="actions-cell">
                        <button 
                          className="btn-icon" 
                          title="Voir les détails"
                          onClick={() => navigate(`/tournament/${tournament.id}`)}
                        >
                          <i className="fas fa-eye"></i>
                        </button>
                        <button 
                          className="btn-icon" 
                          title="Modifier"
                          onClick={() => handleEdit(tournament.id)}
                        >
                          <i className="fas fa-edit"></i>
                        </button>
                        <button 
                          className="btn-icon btn-danger" 
                          title="Supprimer"
                          onClick={() => openConfirmModal(tournament.id)}
                        >
                          <i className="fas fa-trash"></i>
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Modal de confirmation de suppression */}
      {showConfirmModal && (
        <div className="modal-overlay">
          <div className="confirm-modal">
            <h3>Confirmer la suppression</h3>
            <p>Êtes-vous sûr de vouloir supprimer ce tournoi ? Cette action est irréversible.</p>
            <div className="modal-actions">
              <button 
                className="btn-secondary" 
                onClick={closeConfirmModal}
              >
                Annuler
              </button>
              <button 
                className="btn-danger" 
                onClick={handleDelete}
              >
                Supprimer
              </button>
            </div>
          </div>
        </div>
      )}
    </AdminLayout>
  );
};

export default TournamentList; 