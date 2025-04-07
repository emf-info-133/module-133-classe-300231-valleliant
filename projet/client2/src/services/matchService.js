// Données fictives pour les matchs
let mockMatches = [
  {
    id: 1,
    tournamentId: 1,
    teamAId: 1,
    teamBId: 2,
    scoreA: 3,
    scoreB: 1,
    date: '2023-05-20T14:00:00.000Z',
    status: 'completed',
    location: 'Arena Principal'
  },
  {
    id: 2,
    tournamentId: 1,
    teamAId: 1,
    teamBId: 2,
    scoreA: null,
    scoreB: null,
    date: '2023-06-05T15:30:00.000Z',
    status: 'scheduled',
    location: 'Arena Principal'
  },
  {
    id: 3,
    tournamentId: 2,
    teamAId: 3,
    teamBId: null, // À déterminer
    scoreA: null,
    scoreB: null,
    date: '2023-07-10T16:00:00.000Z',
    status: 'scheduled',
    location: 'Stade Régional'
  }
];

// Récupérer tous les matchs
export const getAllMatches = async () => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 500));
  return [...mockMatches];
};

// Récupérer les matchs d'un tournoi spécifique
export const getMatchesByTournament = async (tournamentId) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 400));
  
  const tid = parseInt(tournamentId, 10);
  return mockMatches.filter(match => match.tournamentId === tid);
};

// Récupérer les matchs d'une équipe spécifique
export const getMatchesByTeam = async (teamId) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 400));
  
  const tid = parseInt(teamId, 10);
  return mockMatches.filter(match => match.teamAId === tid || match.teamBId === tid);
};

// Récupérer un match par son ID
export const getMatchById = async (id) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 300));
  
  const matchId = parseInt(id, 10);
  const match = mockMatches.find(m => m.id === matchId);
  
  if (!match) {
    throw new Error('Match non trouvé');
  }
  
  return { ...match };
};

// Créer un nouveau match
export const createMatch = async (matchData) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 800));
  
  // Générer un nouvel ID
  const newId = Math.max(...mockMatches.map(m => m.id), 0) + 1;
  
  const newMatch = {
    id: newId,
    ...matchData,
    scoreA: matchData.scoreA !== undefined ? matchData.scoreA : null,
    scoreB: matchData.scoreB !== undefined ? matchData.scoreB : null,
    status: matchData.status || 'scheduled'
  };
  
  mockMatches.push(newMatch);
  
  return { ...newMatch };
};

// Mettre à jour un match existant
export const updateMatch = async (id, matchData) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 800));
  
  const matchId = parseInt(id, 10);
  const index = mockMatches.findIndex(m => m.id === matchId);
  
  if (index === -1) {
    throw new Error('Match non trouvé');
  }
  
  // Mettre à jour les données
  mockMatches[index] = {
    ...mockMatches[index],
    ...matchData
  };
  
  return { ...mockMatches[index] };
};

// Supprimer un match
export const deleteMatch = async (id) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 800));
  
  const matchId = parseInt(id, 10);
  const initialLength = mockMatches.length;
  
  mockMatches = mockMatches.filter(m => m.id !== matchId);
  
  if (mockMatches.length === initialLength) {
    throw new Error('Match non trouvé');
  }
  
  return { success: true };
};

// Mettre à jour le score d'un match
export const updateMatchScore = async (id, scoreA, scoreB) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 500));
  
  const matchId = parseInt(id, 10);
  const index = mockMatches.findIndex(m => m.id === matchId);
  
  if (index === -1) {
    throw new Error('Match non trouvé');
  }
  
  // Mettre à jour le score
  mockMatches[index] = {
    ...mockMatches[index],
    scoreA: scoreA,
    scoreB: scoreB,
    status: 'completed'
  };
  
  return { ...mockMatches[index] };
}; 