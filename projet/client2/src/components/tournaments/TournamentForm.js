import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './TournamentForm.css';

const TournamentForm = ({ initialData, onSubmit }) => {
  const navigate = useNavigate();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [formError, setFormError] = useState(null);
  const [formSuccess, setFormSuccess] = useState(null);
  const [form, setForm] = useState({
    name: '',
    description: '',
    startDate: '',
    endDate: '',
    location: '',
    maxTeams: 8,
    status: 'upcoming',
    gameType: 'Rocket League',
    logoUrl: '',
    prizePool: ''
  });

  // Si des données initiales sont fournies, mettre à jour le formulaire
  useEffect(() => {
    if (initialData) {
      // Conversion des dates en format YYYY-MM-DD pour les champs de type date
      const formattedInitialData = {
        ...initialData,
        startDate: initialData.startDate ? new Date(initialData.startDate).toISOString().split('T')[0] : '',
        endDate: initialData.endDate ? new Date(initialData.endDate).toISOString().split('T')[0] : ''
      };
      setForm(formattedInitialData);
    }
  }, [initialData]);

  const handleChange = (e) => {
    const { name, value, type } = e.target;
    const newValue = type === 'number' ? (value === '' ? '' : Number(value)) : value;
    setForm(prev => ({ ...prev, [name]: newValue }));
  };

  const validateForm = () => {
    // Liste des erreurs potentielles
    const errors = [];

    if (!form.name.trim()) errors.push('Le nom du tournoi est requis');
    if (!form.startDate) errors.push('La date de début est requise');
    if (!form.endDate) errors.push('La date de fin est requise');
    if (new Date(form.startDate) > new Date(form.endDate)) {
      errors.push('La date de début doit être antérieure à la date de fin');
    }
    if (form.maxTeams <= 0) errors.push('Le nombre maximum d\'équipes doit être supérieur à 0');

    return errors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Vérification du formulaire
    const validationErrors = validateForm();
    if (validationErrors.length > 0) {
      setFormError(validationErrors.join(', '));
      return;
    }

    setIsSubmitting(true);
    setFormError(null);
    setFormSuccess(null);

    try {
      const result = await onSubmit(form);
      if (result) {
        setFormSuccess('Tournoi enregistré avec succès!');
        // Redirection après 1,5 seconde pour laisser l'utilisateur voir le message de succès
        setTimeout(() => {
          navigate('/tournaments');
        }, 1500);
      }
    } catch (error) {
      console.error('Erreur lors de l\'enregistrement du tournoi:', error);
      setFormError(error.response?.data?.message || 'Une erreur est survenue lors de l\'enregistrement du tournoi.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="tournament-form-container">
      {formError && <div className="error-message">{formError}</div>}
      {formSuccess && <div className="success-message">{formSuccess}</div>}
      
      <form onSubmit={handleSubmit} className="tournament-form">
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="name">Nom du tournoi *</label>
            <input
              type="text"
              id="name"
              name="name"
              value={form.name}
              onChange={handleChange}
              placeholder="Nom du tournoi"
              required
              disabled={isSubmitting}
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="gameType">Type de jeu *</label>
            <select
              id="gameType"
              name="gameType"
              value={form.gameType}
              onChange={handleChange}
              required
              disabled={isSubmitting}
            >
              <option value="Rocket League">Rocket League</option>
              <option value="League of Legends">League of Legends</option>
              <option value="Valorant">Valorant</option>
              <option value="Counter-Strike">Counter-Strike</option>
              <option value="Fortnite">Fortnite</option>
              <option value="Overwatch">Overwatch</option>
              <option value="Apex Legends">Apex Legends</option>
              <option value="Hearthstone">Hearthstone</option>
              <option value="Dota 2">Dota 2</option>
              <option value="Rainbow Six Siege">Rainbow Six Siege</option>
              <option value="Call of Duty">Call of Duty</option>
              <option value="FIFA">FIFA</option>
              <option value="Autre">Autre</option>
            </select>
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="startDate">Date de début *</label>
            <input
              type="date"
              id="startDate"
              name="startDate"
              value={form.startDate}
              onChange={handleChange}
              required
              disabled={isSubmitting}
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="endDate">Date de fin *</label>
            <input
              type="date"
              id="endDate"
              name="endDate"
              value={form.endDate}
              onChange={handleChange}
              required
              disabled={isSubmitting}
            />
          </div>
        </div>
        
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="location">Lieu</label>
            <input
              type="text"
              id="location"
              name="location"
              value={form.location}
              onChange={handleChange}
              placeholder="Lieu du tournoi"
              disabled={isSubmitting}
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="maxTeams">Nombre maximum d'équipes *</label>
            <input
              type="number"
              id="maxTeams"
              name="maxTeams"
              value={form.maxTeams}
              onChange={handleChange}
              min="2"
              required
              disabled={isSubmitting}
            />
          </div>
        </div>
        
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="status">Statut *</label>
            <select
              id="status"
              name="status"
              value={form.status}
              onChange={handleChange}
              required
              disabled={isSubmitting}
            >
              <option value="upcoming">À venir</option>
              <option value="in_progress">En cours</option>
              <option value="completed">Terminé</option>
              <option value="cancelled">Annulé</option>
            </select>
          </div>
          
          <div className="form-group">
            <label htmlFor="prizePool">Prix total</label>
            <input
              type="text"
              id="prizePool"
              name="prizePool"
              value={form.prizePool}
              onChange={handleChange}
              placeholder="ex: 1000 CHF"
              disabled={isSubmitting}
            />
          </div>
        </div>
        
        <div className="form-group">
          <label htmlFor="logoUrl">URL du logo</label>
          <input
            type="text"
            id="logoUrl"
            name="logoUrl"
            value={form.logoUrl}
            onChange={handleChange}
            placeholder="https://example.com/logo.png"
            disabled={isSubmitting}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={form.description}
            onChange={handleChange}
            rows="4"
            placeholder="Description détaillée du tournoi"
            disabled={isSubmitting}
          ></textarea>
        </div>
        
        <div className="form-actions">
          <button 
            type="button" 
            className="btn btn-secondary"
            onClick={() => navigate('/tournaments')}
            disabled={isSubmitting}
          >
            Annuler
          </button>
          <button 
            type="submit" 
            className="btn btn-primary"
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Enregistrement...' : (initialData ? 'Mettre à jour' : 'Créer le tournoi')}
          </button>
        </div>
      </form>
    </div>
  );
};

export default TournamentForm; 