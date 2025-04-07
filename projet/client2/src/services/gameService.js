// Données fictives pour les jeux
const mockGames = [
  {
    id: 1,
    name: 'League of Legends',
    description: 'MOBA populaire développé par Riot Games',
    image: 'https://via.placeholder.com/150/3498db/FFFFFF?text=LoL',
    teamSize: 5
  },
  {
    id: 2,
    name: 'Counter-Strike 2',
    description: 'FPS tactique développé par Valve',
    image: 'https://via.placeholder.com/150/2ecc71/FFFFFF?text=CS2',
    teamSize: 5
  },
  {
    id: 3,
    name: 'Valorant',
    description: 'FPS tactique développé par Riot Games',
    image: 'https://via.placeholder.com/150/e74c3c/FFFFFF?text=Valorant',
    teamSize: 5
  },
  {
    id: 4,
    name: 'Rocket League',
    description: 'Jeu de football avec des voitures, développé par Psyonix',
    image: 'https://via.placeholder.com/150/f39c12/FFFFFF?text=RL',
    teamSize: 3
  },
  {
    id: 5,
    name: 'Overwatch 2',
    description: 'FPS par équipe développé par Blizzard',
    image: 'https://via.placeholder.com/150/9b59b6/FFFFFF?text=OW2',
    teamSize: 5
  }
];

// Récupérer tous les jeux
export const getAllGames = async () => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 500));
  return [...mockGames];
};

// Récupérer un jeu par son ID
export const getGameById = async (id) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 300));
  
  const gameId = parseInt(id, 10);
  const game = mockGames.find(g => g.id === gameId);
  
  if (!game) {
    throw new Error('Jeu non trouvé');
  }
  
  return { ...game };
}; 