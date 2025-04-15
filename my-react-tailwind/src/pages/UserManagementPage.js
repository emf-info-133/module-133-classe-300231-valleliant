import React, { useState, useEffect } from 'react';
import * as api from '../services/api';
// Importer les composants nécessaires
import UserList from '../components/users/UserList';

const UserManagementPage = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredUsers, setFilteredUsers] = useState([]);

   useEffect(() => {
    const fetchUsers = async () => {
      try {
        setLoading(true);
        const response = await api.getAllUsers();
        setUsers(response.data || []);
        setFilteredUsers(response.data || []);
        setError('');
      } catch (err) {
        setError('Erreur lors de la récupération des utilisateurs.');
        console.error(err);
        // Gérer le cas 401/403 spécifiquement si nécessaire
      } finally {
        setLoading(false);
      }
    };
    fetchUsers();
  }, []);

   useEffect(() => {
    // Filtrer les utilisateurs
    const lowercasedFilter = searchTerm.toLowerCase();
    const filtered = users.filter(user => 
        (user.name && user.name.toLowerCase().includes(lowercasedFilter)) || // Vérifier si name existe
        (user.email && user.email.toLowerCase().includes(lowercasedFilter))
    );
    setFilteredUsers(filtered);
  }, [searchTerm, users]);

  // L'API actuelle ne permet pas la création/modification/suppression d'utilisateurs via cette page par l'admin
  // On affiche juste la liste

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-yellow-400">Gestion des Utilisateurs</h1>
         {/* Pas de bouton de création ici car géré par /register */}
      </div>

       {/* Barre de recherche */}
      <div className="mb-4">
            <input 
                type="text"
                placeholder="Rechercher un utilisateur par nom ou email..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full md:w-1/2"
            />
      </div>

      {loading && <p className="text-center text-gray-400">Chargement des utilisateurs...</p>}
      {error && <p className="text-center text-red-500">{error}</p>}

      {!loading && !error && (
          <UserList users={filteredUsers} />
      )}

    </div>
  );
};

export default UserManagementPage; 