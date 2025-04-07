import React from 'react';
import AdminLayout from '../../components/Layout/AdminLayout';
import TournamentForm from '../../components/tournaments/TournamentForm';
import { createTournament } from '../../services/tournamentService';

const CreateTournament = () => {
  const handleSubmit = async (formData) => {
    try {
      await createTournament(formData);
      return true;
    } catch (error) {
      console.error('Erreur lors de la création du tournoi:', error);
      throw error;
    }
  };

  return (
    <AdminLayout>
      <div className="page-container">
        <div className="page-header">
          <h2>Créer un nouveau tournoi</h2>
          <p className="page-subtitle">
            Remplissez le formulaire ci-dessous pour créer un nouveau tournoi.
          </p>
        </div>
        
        <TournamentForm onSubmit={handleSubmit} />
      </div>
    </AdminLayout>
  );
};

export default CreateTournament; 