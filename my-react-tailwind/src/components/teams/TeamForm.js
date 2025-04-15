import React, { useState, useEffect } from 'react';
import * as api from '../../services/api';
import { useAuth } from '../../hooks/useAuth';

const TeamForm = ({ team, onSave, onCancel }) => {
  const { user } = useAuth();
  const [name, setName] = useState('');
  const [game, setGame] = useState('');
  // Le capitaine est défini par l'utilisateur connecté lors de la création
  // Pour l'édition, on pourrait vouloir afficher le capitaine mais pas le modifier ici.
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Pré-remplir le formulaire si une équipe est fournie (mode édition)
  useEffect(() => {
    if (team) {
      setName(team.name || '');
      setGame(team.game || '');
    } else {
      // Réinitialiser pour le mode création
      setName('');
      setGame('');
    }
  }, [team]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    // Données de l'équipe à envoyer
    const teamData = {
      name,
      game,
      // Pour la création, le backend devrait idéalement utiliser l'ID de l'utilisateur connecté comme capitaine
      // Si l'API attend `captain` explicitement pour la création:
      // captain: team ? team.captain : user?.id, 
      // Assurez-vous que votre API gère la création correctement (prend l'ID de session ou attend `captain`)
    };

    // Si on est en mode création et que l'API attend l'ID du capitaine
    if (!team && user?.id) {
        teamData.captain = user.id; 
    }
    // Si en mode édition, on n'envoie pas le capitaine car il ne devrait pas être modifiable via ce formulaire
    // (ou alors, seul l'admin pourrait le faire, mais l'API ne semble pas le supporter directement)

    try {
      let savedTeam;
      if (team) {
        // Mode Édition
        // L'API updateTeam prend (id, teamDTO). teamDTO ne nécessite pas forcément `captain`.
        const updateData = { name, game }; // On envoie seulement les champs modifiables
        await api.updateTeam(team.id, updateData);
        savedTeam = { ...team, ...updateData }; // Mettre à jour l'objet local
      } else {
        // Mode Création
        // L'API createTeam prend teamDTO qui contient `name`, `game`, `captain`.
        const response = await api.createTeam(teamData);
        savedTeam = response.data; // L'API retourne l'équipe créée avec son ID
      }
      onSave(savedTeam); // Appeler la fonction de rappel avec l'équipe sauvegardée
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la sauvegarde de l\'équipe.');
      console.error("Erreur sauvegarde équipe:", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label htmlFor="team-name" className="block text-sm font-medium text-gray-300 mb-1">Nom de l'équipe</label>
        <input
          id="team-name"
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
          placeholder="Ex : Les Aigles Rouges"
          className="w-full"
        />
      </div>
      <div>
        <label htmlFor="team-game" className="block text-sm font-medium text-gray-300 mb-1">Jeu Principal</label>
        <input
          id="team-game"
          type="text"
          value={game}
          onChange={(e) => setGame(e.target.value)}
          placeholder="Ex : League of Legends, Valorant..."
          className="w-full"
        />
         <p className="text-xs text-gray-400 mt-1">Optionnel, mais recommandé.</p>
      </div>

       {/* Afficher le capitaine (non modifiable ici) */}
       {team && (
           <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Capitaine (ID)</label>
                <p className="text-gray-400 bg-gray-700 px-3 py-2 rounded-md">{team.captain} (non modifiable)</p>
           </div>
       )}
        {!team && (
           <div>
                <label className="block text-sm font-medium text-gray-300 mb-1">Capitaine</label>
                <p className="text-gray-400 bg-gray-700 px-3 py-2 rounded-md">{user?.name || user?.email} (Vous)</p>
           </div>
       )}

      {error && (
        <div className="text-red-500 text-sm text-center p-3 bg-red-900 border border-red-700 rounded">
            {error}
        </div>
      )}

      <div className="flex justify-end space-x-3 pt-4">
        <button 
          type="button" 
          onClick={onCancel} 
          className="btn-secondary"
          disabled={loading}
        >
          Annuler
        </button>
        <button 
          type="submit" 
          className="btn-primary"
          disabled={loading}
        >
          {loading ? 'Sauvegarde...' : (team ? 'Mettre à jour' : 'Créer l\'équipe')}
        </button>
      </div>
    </form>
  );
};

export default TeamForm; 