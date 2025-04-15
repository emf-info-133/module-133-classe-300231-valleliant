import React, { useState, useEffect } from 'react';
import { useAuth } from '../hooks/useAuth';
import * as api from '../services/api'; // Importer toutes les fonctions api
import TeamList from '../components/teams/TeamList'; // À créer
import TeamForm from '../components/teams/TeamForm'; // À créer
import Modal from '../components/common/Modal'; // Composant modal générique (à créer)

const TeamManagementPage = () => {
  const { user, isAdmin } = useAuth();
  const [teams, setTeams] = useState([]);
  const [filteredTeams, setFilteredTeams] = useState([]); // Pour le filtre éventuel
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [selectedTeam, setSelectedTeam] = useState(null); // Pour l'édition
  const [searchTerm, setSearchTerm] = useState(''); // Pour la recherche

  useEffect(() => {
    const fetchTeams = async () => {
      try {
        setLoading(true);
        const response = await api.getAllTeams();
        setTeams(response.data || []);
        setFilteredTeams(response.data || []);
        setError('');
      } catch (err) {
        setError('Erreur lors de la récupération des équipes.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchTeams();
  }, []);

  useEffect(() => {
    // Filtrer les équipes quand le terme de recherche change
    const lowercasedFilter = searchTerm.toLowerCase();
    const filtered = teams.filter(team => 
        team.name.toLowerCase().includes(lowercasedFilter)
        // Ajoutez d'autres champs de recherche si nécessaire (ex: nom du capitaine)
    );
    setFilteredTeams(filtered);
  }, [searchTerm, teams]);


  const handleCreate = () => {
    setSelectedTeam(null); // Assure qu'on est en mode création
    setShowModal(true);
  };

  const handleEdit = (team) => {
    // Vérification des droits : Seul le capitaine ou l'admin peut éditer
    if (isAdmin || team.captain === user?.id) { // Assurez-vous que team.captain contient l'ID user
      setSelectedTeam(team);
      setShowModal(true);
    } else {
        alert("Vous n'avez pas les droits pour modifier cette équipe.");
    }
  };

  const handleDelete = async (teamId) => {
     // Vérification des droits : Seul le capitaine ou l'admin peut supprimer
     const teamToDelete = teams.find(t => t.id === teamId);
     if (!teamToDelete) return;

     if (isAdmin || teamToDelete.captain === user?.id) { // Assurez-vous que team.captain contient l'ID user
         if (window.confirm(`Êtes-vous sûr de vouloir supprimer l'équipe "${teamToDelete.name}" ?`)) {
            try {
                await api.deleteTeam(teamId);
                // Mettre à jour la liste après suppression
                setTeams(prevTeams => prevTeams.filter(t => t.id !== teamId));
            } catch (err) {
                setError('Erreur lors de la suppression de l\'équipe.');
                console.error(err);
            }
        }
     } else {
         alert("Vous n'avez pas les droits pour supprimer cette équipe.");
     }
  };

  const handleSave = (savedTeam) => {
    if (selectedTeam) { // Mode édition
      setTeams(teams.map(t => t.id === savedTeam.id ? savedTeam : t));
    } else { // Mode création
      setTeams([...teams, savedTeam]);
    }
    setShowModal(false);
    setSelectedTeam(null);
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-indigo-400">
            {isAdmin ? 'Gestion des Équipes' : 'Mes Équipes'}
        </h1>
        <button onClick={handleCreate} className="btn-primary">
          Créer une Équipe
        </button>
      </div>

       {/* Barre de recherche */}
      <div className="mb-4">
            <input 
                type="text"
                placeholder="Rechercher une équipe par nom..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full md:w-1/3"
            />
      </div>

      {loading && <p className="text-center text-gray-400">Chargement des équipes...</p>}
      {error && <p className="text-center text-red-500">{error}</p>}
      
      {!loading && !error && (
        <TeamList 
          teams={filteredTeams} 
          onEdit={handleEdit} 
          onDelete={handleDelete} 
          currentUser={user} // Passer l'utilisateur actuel pour vérifier les droits dans TeamList si besoin
          isAdmin={isAdmin}
        />
      )}

      {showModal && (
        <Modal title={selectedTeam ? 'Modifier l\'Équipe' : 'Créer une Équipe'} onClose={() => setShowModal(false)}>
          <TeamForm 
            team={selectedTeam} 
            onSave={handleSave} 
            onCancel={() => setShowModal(false)} 
          />
        </Modal>
      )}
    </div>
  );
};

export default TeamManagementPage; 