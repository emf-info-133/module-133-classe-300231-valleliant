import React, { useState, useEffect } from 'react';
import * as api from '../services/api';
// Importer les composants nécessaires
import TournamentList from '../components/tournaments/TournamentList';
import TournamentForm from '../components/tournaments/TournamentForm';
import Modal from '../components/common/Modal';

const TournamentManagementPage = () => {
  const [tournaments, setTournaments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [selectedTournament, setSelectedTournament] = useState(null);

   useEffect(() => {
    const fetchTournaments = async () => {
      try {
        setLoading(true);
        const response = await api.getAllTournaments();
        setTournaments(response.data || []);
        setError('');
      } catch (err) {
        setError('Erreur lors de la récupération des tournois.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchTournaments();
  }, []);

  const handleCreate = () => {
    setSelectedTournament(null);
    setShowModal(true);
  };

  const handleEdit = (tournament) => {
    setSelectedTournament(tournament);
    setShowModal(true);
  };

  const handleDelete = async (tournamentId) => {
    const tournamentToDelete = tournaments.find(t => t.id === tournamentId);
     if (!tournamentToDelete) return;

     if (window.confirm(`Êtes-vous sûr de vouloir supprimer le tournoi "${tournamentToDelete.name}" ?`)) {
        try {
            await api.deleteTournament(tournamentId);
            setTournaments(prev => prev.filter(t => t.id !== tournamentId));
        } catch (err) {
            setError('Erreur lors de la suppression du tournoi.');
            console.error(err);
        }
    }
  };

  const handleSave = (savedTournament) => {
    if (selectedTournament) {
      setTournaments(tournaments.map(t => t.id === savedTournament.id ? savedTournament : t));
    } else {
      setTournaments([...tournaments, savedTournament]);
    }
    setShowModal(false);
    setSelectedTournament(null);
  };


  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-yellow-400">Gestion des Tournois</h1>
        <button onClick={handleCreate} className="btn-primary">
          Créer un Tournoi
        </button>
      </div>

      {loading && <p className="text-center text-gray-400">Chargement des tournois...</p>}
      {error && <p className="text-center text-red-500">{error}</p>}

      {!loading && !error && (
          <TournamentList 
            tournaments={tournaments} 
            onEdit={handleEdit} 
            onDelete={handleDelete} 
          />
      )}

      {showModal && (
        <Modal title={selectedTournament ? 'Modifier le Tournoi' : 'Créer un Tournoi'} onClose={() => setShowModal(false)}>
          <TournamentForm 
            tournament={selectedTournament} 
            onSave={handleSave} 
            onCancel={() => setShowModal(false)} 
          />
        </Modal>
      )}
    </div>
  );
};

export default TournamentManagementPage; 