import React, { useState, useEffect } from 'react';
import * as api from '../services/api';
// Importer les composants nécessaires
import MatchList from '../components/matches/MatchList';
import MatchForm from '../components/matches/MatchForm';
import Modal from '../components/common/Modal';

const MatchManagementPage = () => {
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [selectedMatch, setSelectedMatch] = useState(null);

   useEffect(() => {
    const fetchMatches = async () => {
      try {
        setLoading(true);
        const response = await api.getAllMatches();
        setMatches(response.data || []);
        setError('');
      } catch (err) {
        setError('Erreur lors de la récupération des matchs.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchMatches();
  }, []);

  const handleCreate = () => {
    setSelectedMatch(null);
    setShowModal(true);
  };

  const handleEdit = (match) => {
    setSelectedMatch(match);
    setShowModal(true);
  };

  const handleDelete = async (matchId) => {
    const matchToDelete = matches.find(m => m.id === matchId);
     if (!matchToDelete) return;

     if (window.confirm(`Êtes-vous sûr de vouloir supprimer le match ID: ${matchId} ?`)) {
        try {
            await api.deleteMatch(matchId);
            setMatches(prev => prev.filter(m => m.id !== matchId));
        } catch (err) {
            setError('Erreur lors de la suppression du match.');
            console.error(err);
        }
    }
  };

  const handleSave = (savedMatch) => {
    if (selectedMatch) {
      setMatches(matches.map(m => m.id === savedMatch.id ? savedMatch : m));
    } else {
      setMatches([...matches, savedMatch]);
    }
    setShowModal(false);
    setSelectedMatch(null);
  };


  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-yellow-400">Gestion des Matchs</h1>
        <button onClick={handleCreate} className="btn-primary">
          Créer un Match
        </button>
      </div>

      {loading && <p className="text-center text-gray-400">Chargement des matchs...</p>}
      {error && <p className="text-center text-red-500">{error}</p>}

      {!loading && !error && (
          <MatchList 
            matches={matches} 
            onEdit={handleEdit} 
            onDelete={handleDelete} 
          />
      )}

      {showModal && (
        <Modal title={selectedMatch ? 'Modifier le Match' : 'Créer un Match'} onClose={() => setShowModal(false)}>
          <MatchForm 
            match={selectedMatch} 
            onSave={handleSave} 
            onCancel={() => setShowModal(false)} 
          />
        </Modal>
      )}
    </div>
  );
};

export default MatchManagementPage; 