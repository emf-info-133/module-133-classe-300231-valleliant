import { mockUsers } from './userService';

// Données fictives pour les équipes
let mockTeams = [
  {
    id: 1,
    name: 'Team Alpha',
    description: 'Équipe professionnelle avec plusieurs années d\'expérience',
    tournamentId: 1,
    captainId: 2,
    maxMembers: 5,
    members: [
      { id: 2, name: 'Marie Dupont', role: 'player', joinDate: '2023-01-15' },
      { id: 3, name: 'Pierre Martin', role: 'player', joinDate: '2023-01-16' }
    ]
  },
  {
    id: 2,
    name: 'Les Invincibles',
    description: 'Équipe amateur mais très motivée',
    tournamentId: 1,
    captainId: 4,
    maxMembers: 5,
    members: [
      { id: 4, name: 'Sophie Bernard', role: 'player', joinDate: '2023-02-01' }
    ]
  },
  {
    id: 3,
    name: 'Fury Gaming',
    description: 'Équipe semi-pro en pleine ascension',
    tournamentId: 2,
    captainId: 5,
    maxMembers: 5,
    members: [
      { id: 5, name: 'Thomas Petit', role: 'player', joinDate: '2023-01-20' },
      { id: 6, name: 'Julie Moreau', role: 'player', joinDate: '2023-01-25' },
      { id: 7, name: 'Nicolas Lefebvre', role: 'player', joinDate: '2023-02-05' }
    ]
  }
];

// Récupérer toutes les équipes
export const getAllTeams = async () => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 500));
  return [...mockTeams];
};

// Récupérer les équipes d'un tournoi spécifique
export const getTeamsByTournament = async (tournamentId) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 400));
  
  const tid = parseInt(tournamentId, 10);
  return mockTeams.filter(team => team.tournamentId === tid);
};

// Récupérer une équipe par son ID
export const getTeamById = async (id) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 300));
  
  const teamId = parseInt(id, 10);
  const team = mockTeams.find(t => t.id === teamId);
  
  if (!team) {
    throw new Error('Équipe non trouvée');
  }
  
  // Récupérer les détails complets du capitaine
  const captain = mockUsers.find(u => u.id === team.captainId);
  
  return { 
    ...team,
    captain: captain || null
  };
};

// Créer une nouvelle équipe
export const createTeam = async (teamData) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 800));
  
  // Générer un nouvel ID
  const newId = Math.max(...mockTeams.map(t => t.id), 0) + 1;
  
  const newTeam = {
    id: newId,
    ...teamData,
    members: teamData.members || []
  };
  
  mockTeams.push(newTeam);
  
  return { ...newTeam };
};

// Mettre à jour une équipe existante
export const updateTeam = async (id, teamData) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 800));
  
  const teamId = parseInt(id, 10);
  const index = mockTeams.findIndex(t => t.id === teamId);
  
  if (index === -1) {
    throw new Error('Équipe non trouvée');
  }
  
  // Mettre à jour les données
  mockTeams[index] = {
    ...mockTeams[index],
    ...teamData
  };
  
  return { ...mockTeams[index] };
};

// Supprimer une équipe
export const deleteTeam = async (id) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 800));
  
  const teamId = parseInt(id, 10);
  const initialLength = mockTeams.length;
  
  mockTeams = mockTeams.filter(t => t.id !== teamId);
  
  if (mockTeams.length === initialLength) {
    throw new Error('Équipe non trouvée');
  }
  
  return { success: true };
}; 