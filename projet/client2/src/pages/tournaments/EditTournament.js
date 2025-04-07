import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import AdminLayout from '../../components/Layout/AdminLayout';
import TournamentForm from '../../components/tournaments/TournamentForm';
import { getTournamentById, updateTournament } from '../../services/tournamentService';

const EditTournament = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [tournament, setTournament] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTournament = async () => {
      try {
        setLoading(true);
        const data = await getTournamentById(id);
        setTournament(data);
      } catch (err) {
        setError('Impossible de récupérer les informations du tournoi.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchTournament();
  }, [id]);

  const handleSubmit = async (formData) => {
    try {
      await updateTournament(id, formData);
      return true;
    } catch (error) {
      console.error('Erreur lors de la mise à jour du tournoi:', error);
      throw error;
    }
  };

  if (loading) {
    return (
      <AdminLayout>
        <div className="loading-indicator">Chargement des données du tournoi...</div>
      </AdminLayout>
    );
  }

  if (error) {
    return (
      <AdminLayout>
        <div className="error-message">{error}</div>
        <button 
          className="btn btn-primary mt-3"
          onClick={() => navigate('/tournaments')}
        >
          Retour à la liste des tournois
        </button>
      </AdminLayout>
    );
  }

  if (!tournament) {
    return (
      <AdminLayout>
        <div className="error-message">Tournoi introuvable.</div>
        <button 
          className="btn btn-primary mt-3"
          onClick={() => navigate('/tournaments')}
        >
          Retour à la liste des tournois
        </button>
      </AdminLayout>
    );
  }

  return (
    <AdminLayout>
      <div className="page-container">
        <div className="page-header">
          <h2>Modifier le tournoi: {tournament.name}</h2>
          <p className="page-subtitle">
            Modifiez les informations du tournoi en utilisant le formulaire ci-dessous.
          </p>
        </div>
        
        <TournamentForm 
          initialData={tournament} 
          onSubmit={handleSubmit} 
        />
      </div>
    </AdminLayout>
  );
};

export default EditTournament; 