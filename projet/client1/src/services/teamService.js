import { mockTeams } from './tournamentService';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

export const getTeamsByTournament = async (tournamentId) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 400));
  
  const tid = parseInt(tournamentId, 10);
  return mockTeams.filter(team => team.tournamentId === tid);
};

export const joinTeam = async (teamId) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 600));
  
  // Récupérer l'utilisateur depuis le localStorage
  const userStr = localStorage.getItem('user');
  if (!userStr) {
    throw new Error('Vous devez être connecté pour rejoindre une équipe');
  }
  
  const user = JSON.parse(userStr);
  const tid = parseInt(teamId, 10);
  
  // Trouver l'équipe
  const team = mockTeams.find(t => t.id === tid);
  if (!team) {
    throw new Error('Équipe non trouvée');
  }
  
  // Vérifier si l'utilisateur est déjà membre
  if (team.members.some(m => m.id === user.id)) {
    throw new Error('Vous êtes déjà membre de cette équipe');
  }
  
  // Vérifier si l'équipe est complète
  if (team.members.length >= team.maxMembers) {
    throw new Error('L\'équipe est complète');
  }
  
  // Ajouter l'utilisateur à l'équipe
  team.members.push({
    id: user.id,
    name: user.name,
    email: user.email
  });
  
  return { ...team };
}; 