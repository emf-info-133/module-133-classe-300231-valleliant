import { mockTeams } from './tournamentService';

// Données fictives pour les classements
const mockRankings = [
  {
    id: 1,
    name: 'Classement Tournoi de Printemps 2023',
    tournamentId: 1,
    teams: [
      {
        teamId: 1,
        position: 1,
        points: 10,
        matchesPlayed: 3,
        wins: 3,
        draws: 0,
        losses: 0
      },
      {
        teamId: 2,
        position: 2,
        points: 7,
        matchesPlayed: 3,
        wins: 2,
        draws: 1,
        losses: 0
      }
    ]
  },
  {
    id: 2,
    name: 'Classement Championnat régional',
    tournamentId: 2,
    teams: []
  },
  {
    id: 3,
    name: 'Classement Coupe des Champions',
    tournamentId: 3,
    teams: []
  }
];

// Récupérer le classement pour un tournoi spécifique
export const getRankingByTournament = async (tournamentId) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 300));
  
  const tid = parseInt(tournamentId, 10);
  const ranking = mockRankings.find(r => r.tournamentId === tid);
  
  if (!ranking) {
    throw new Error('Classement non trouvé');
  }
  
  // Obtenir les détails complets des équipes
  const teamsWithDetails = ranking.teams.map(teamRanking => {
    const team = mockTeams.find(t => t.id === teamRanking.teamId);
    return {
      ...teamRanking,
      team
    };
  });
  
  return {
    ...ranking,
    teamsWithDetails
  };
};

// Mettre à jour le classement d'une équipe
export const updateTeamRanking = async (rankingId, teamId, updateData) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 500));
  
  const rid = parseInt(rankingId, 10);
  const tid = parseInt(teamId, 10);
  
  const ranking = mockRankings.find(r => r.id === rid);
  if (!ranking) {
    throw new Error('Classement non trouvé');
  }
  
  // Trouver l'équipe dans le classement
  const teamIndex = ranking.teams.findIndex(t => t.teamId === tid);
  if (teamIndex === -1) {
    // Si l'équipe n'existe pas dans le classement, l'ajouter
    ranking.teams.push({
      teamId: tid,
      position: ranking.teams.length + 1,
      points: updateData.points || 0,
      matchesPlayed: updateData.matchesPlayed || 0,
      wins: updateData.wins || 0,
      draws: updateData.draws || 0,
      losses: updateData.losses || 0
    });
  } else {
    // Mettre à jour les statistiques de l'équipe
    ranking.teams[teamIndex] = {
      ...ranking.teams[teamIndex],
      ...updateData
    };
  }
  
  // Recalculer les positions en fonction des points
  ranking.teams.sort((a, b) => b.points - a.points);
  ranking.teams.forEach((team, index) => {
    team.position = index + 1;
  });
  
  return { success: true };
}; 