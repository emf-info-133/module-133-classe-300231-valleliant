import axios from 'axios';
import { API_URL } from '../config';

// URL de base pour les classements
const RANKING_API = `${API_URL}/service2/rankings`;

// Récupérer tous les classements
export const getAllRankings = async () => {
  try {
    const response = await axios.get(RANKING_API, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des classements:', error);
    throw error;
  }
};

// Récupérer le classement d'un tournoi
export const getRankingByTournament = async (tournamentId) => {
  try {
    const response = await axios.get(`${RANKING_API}/tournament/${tournamentId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du classement du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

// Mettre à jour le classement d'un tournoi
export const updateRanking = async (tournamentId, rankingData) => {
  try {
    const response = await axios.put(`${RANKING_API}/tournament/${tournamentId}`, rankingData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la mise à jour du classement du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

// Calcule automatiquement le classement en fonction des résultats des matchs
export const calculateRanking = async (tournamentId) => {
  try {
    const response = await axios.post(`${RANKING_API}/calculate/${tournamentId}`, {}, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('adminToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error(`Erreur lors du calcul du classement du tournoi ${tournamentId}:`, error);
    throw error;
  }
};

// Mettre à jour le classement d'une équipe
export const updateTeamRanking = async (rankingId, teamId, updateData) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 800));
  
  const rid = parseInt(rankingId, 10);
  const tid = parseInt(teamId, 10);
  
  const rankingIndex = mockRankings.findIndex(r => r.id === rid);
  if (rankingIndex === -1) {
    throw new Error('Classement non trouvé');
  }
  
  // Trouver l'équipe dans le classement
  const teamIndex = mockRankings[rankingIndex].teams.findIndex(t => t.teamId === tid);
  
  if (teamIndex === -1) {
    // Si l'équipe n'existe pas dans le classement, l'ajouter
    mockRankings[rankingIndex].teams.push({
      teamId: tid,
      position: mockRankings[rankingIndex].teams.length + 1,
      points: updateData.points || 0,
      matchesPlayed: updateData.matchesPlayed || 0,
      wins: updateData.wins || 0,
      draws: updateData.draws || 0,
      losses: updateData.losses || 0
    });
  } else {
    // Mettre à jour les statistiques de l'équipe
    mockRankings[rankingIndex].teams[teamIndex] = {
      ...mockRankings[rankingIndex].teams[teamIndex],
      ...updateData
    };
  }
  
  // Recalculer les positions en fonction des points
  mockRankings[rankingIndex].teams.sort((a, b) => b.points - a.points);
  mockRankings[rankingIndex].teams.forEach((team, index) => {
    team.position = index + 1;
  });
  
  return { success: true };
};

// Créer un nouveau classement pour un tournoi
export const createRanking = async (tournamentId) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 800));
  
  const tid = parseInt(tournamentId, 10);
  
  // Vérifier si un classement existe déjà pour ce tournoi
  const existingRanking = mockRankings.find(r => r.tournamentId === tid);
  if (existingRanking) {
    throw new Error('Un classement existe déjà pour ce tournoi');
  }
  
  // Générer un nouvel ID
  const newId = Math.max(...mockRankings.map(r => r.id), 0) + 1;
  
  const newRanking = {
    id: newId,
    tournamentId: tid,
    teams: []
  };
  
  mockRankings.push(newRanking);
  
  return { ...newRanking };
};

// Supprimer un classement
export const deleteRanking = async (id) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 800));
  
  const rankingId = parseInt(id, 10);
  const initialLength = mockRankings.length;
  
  mockRankings = mockRankings.filter(r => r.id !== rankingId);
  
  if (mockRankings.length === initialLength) {
    throw new Error('Classement non trouvé');
  }
  
  return { success: true };
};

// Mettre à jour un classement après un match
export const updateRankingAfterMatch = async (tournamentId, teamAId, teamBId, scoreA, scoreB) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 1000));
  
  const tid = parseInt(tournamentId, 10);
  const taId = parseInt(teamAId, 10);
  const tbId = parseInt(teamBId, 10);
  
  const rankingIndex = mockRankings.findIndex(r => r.tournamentId === tid);
  if (rankingIndex === -1) {
    throw new Error('Classement non trouvé');
  }
  
  // Déterminer le vainqueur et les points à attribuer
  let pointsA = 0;
  let pointsB = 0;
  let isWinA = false;
  let isWinB = false;
  let isDraw = false;
  
  if (scoreA > scoreB) {
    pointsA = 3;
    isWinA = true;
  } else if (scoreB > scoreA) {
    pointsB = 3;
    isWinB = true;
  } else {
    pointsA = 1;
    pointsB = 1;
    isDraw = true;
  }
  
  // Mettre à jour l'équipe A
  const teamAIndex = mockRankings[rankingIndex].teams.findIndex(t => t.teamId === taId);
  if (teamAIndex === -1) {
    // Ajouter l'équipe A si elle n'existe pas
    mockRankings[rankingIndex].teams.push({
      teamId: taId,
      position: mockRankings[rankingIndex].teams.length + 1,
      points: pointsA,
      matchesPlayed: 1,
      wins: isWinA ? 1 : 0,
      draws: isDraw ? 1 : 0,
      losses: isWinB ? 1 : 0
    });
  } else {
    // Mettre à jour l'équipe A
    const teamA = mockRankings[rankingIndex].teams[teamAIndex];
    teamA.points += pointsA;
    teamA.matchesPlayed += 1;
    if (isWinA) teamA.wins += 1;
    if (isDraw) teamA.draws += 1;
    if (isWinB) teamA.losses += 1;
  }
  
  // Mettre à jour l'équipe B
  const teamBIndex = mockRankings[rankingIndex].teams.findIndex(t => t.teamId === tbId);
  if (teamBIndex === -1) {
    // Ajouter l'équipe B si elle n'existe pas
    mockRankings[rankingIndex].teams.push({
      teamId: tbId,
      position: mockRankings[rankingIndex].teams.length + 1,
      points: pointsB,
      matchesPlayed: 1,
      wins: isWinB ? 1 : 0,
      draws: isDraw ? 1 : 0,
      losses: isWinA ? 1 : 0
    });
  } else {
    // Mettre à jour l'équipe B
    const teamB = mockRankings[rankingIndex].teams[teamBIndex];
    teamB.points += pointsB;
    teamB.matchesPlayed += 1;
    if (isWinB) teamB.wins += 1;
    if (isDraw) teamB.draws += 1;
    if (isWinA) teamB.losses += 1;
  }
  
  // Recalculer les positions
  mockRankings[rankingIndex].teams.sort((a, b) => b.points - a.points);
  mockRankings[rankingIndex].teams.forEach((team, index) => {
    team.position = index + 1;
  });
  
  return { success: true };
}; 