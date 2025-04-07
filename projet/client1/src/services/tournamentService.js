import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Données fictives pour les tournois
const mockTournaments = [
  {
    id: 1,
    name: 'Tournoi de Printemps 2023',
    description: 'Compétition annuelle ouverte à tous les joueurs',
    startDate: '2023-04-15',
    endDate: '2023-04-30',
    teams: []
  },
  {
    id: 2,
    name: 'Championnat régional',
    description: 'Championnat officiel de la région',
    startDate: '2023-05-10',
    endDate: '2023-05-25',
    teams: []
  },
  {
    id: 3,
    name: 'Coupe des Champions',
    description: 'Tournoi réservé aux équipes d\'élite',
    startDate: '2023-06-05',
    endDate: '2023-06-20',
    teams: []
  }
];

// Données fictives pour les équipes
export const mockTeams = [
  {
    id: 1,
    name: 'Les Aigles',
    tournamentId: 1,
    captain: { id: 1, name: 'Utilisateur Test', email: 'test@example.com' },
    members: [
      { id: 1, name: 'Utilisateur Test', email: 'test@example.com' }
    ],
    maxMembers: 5
  },
  {
    id: 2,
    name: 'Les Loups',
    tournamentId: 1,
    captain: { id: 2, name: 'Admin', email: 'admin@example.com' },
    members: [
      { id: 2, name: 'Admin', email: 'admin@example.com' }
    ],
    maxMembers: 5
  }
];

// Lier les équipes aux tournois
mockTeams.forEach(team => {
  const tournament = mockTournaments.find(t => t.id === team.tournamentId);
  if (tournament) {
    tournament.teams.push(team);
  }
});

export const getAllTournaments = async () => {
  try {
    const response = await axios.get(`${API_URL}/service2/tournaments`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des tournois:', error);
    throw error;
  }
};

export const getTournamentById = async (tournamentId) => {
  try {
    const response = await axios.get(`${API_URL}/service2/tournaments/${tournamentId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

export const getOpenTournaments = async () => {
  try {
    // Les tournois ouverts ont status = true
    const response = await axios.get(`${API_URL}/service2/tournaments/open`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des tournois ouverts:', error);
    throw error;
  }
};

export const getTournamentMatches = async (tournamentId) => {
  try {
    const response = await axios.get(`${API_URL}/service2/tournaments/${tournamentId}/matches`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération des matchs du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

export const getTournamentRankings = async (tournamentId) => {
  try {
    const response = await axios.get(`${API_URL}/service2/tournaments/${tournamentId}/rankings`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du classement du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

export const createTeam = async (tournamentId, teamName) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 700));
  
  // Récupérer l'utilisateur depuis le localStorage (simulation d'un utilisateur connecté)
  const userStr = localStorage.getItem('user');
  if (!userStr) {
    throw new Error('Vous devez être connecté pour créer une équipe');
  }
  
  const user = JSON.parse(userStr);
  const tid = parseInt(tournamentId, 10);
  
  // Vérifier si le tournoi existe
  const tournament = mockTournaments.find(t => t.id === tid);
  if (!tournament) {
    throw new Error('Tournoi non trouvé');
  }
  
  // Créer une nouvelle équipe
  const newTeam = {
    id: mockTeams.length + 1,
    name: teamName,
    tournamentId: tid,
    captain: {
      id: user.id,
      name: user.name,
      email: user.email
    },
    members: [
      {
        id: user.id,
        name: user.name,
        email: user.email
      }
    ],
    maxMembers: 5
  };
  
  // Ajouter l'équipe à la liste et au tournoi
  mockTeams.push(newTeam);
  tournament.teams.push(newTeam);
  
  return { ...newTeam };
}; 