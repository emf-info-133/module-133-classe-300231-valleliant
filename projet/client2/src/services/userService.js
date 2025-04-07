// Données fictives pour les utilisateurs
export const mockUsers = [
  {
    id: 1,
    name: 'Admin Principal',
    email: 'admin@example.com',
    role: 'admin',
    joinDate: '2023-01-01'
  },
  {
    id: 2,
    name: 'Marie Dupont',
    email: 'marie@example.com',
    role: 'player',
    joinDate: '2023-01-15'
  },
  {
    id: 3,
    name: 'Pierre Martin',
    email: 'pierre@example.com',
    role: 'player',
    joinDate: '2023-01-16'
  },
  {
    id: 4,
    name: 'Sophie Bernard',
    email: 'sophie@example.com',
    role: 'player',
    joinDate: '2023-02-01'
  },
  {
    id: 5,
    name: 'Thomas Petit',
    email: 'thomas@example.com',
    role: 'player',
    joinDate: '2023-01-20'
  },
  {
    id: 6,
    name: 'Julie Moreau',
    email: 'julie@example.com',
    role: 'player',
    joinDate: '2023-01-25'
  },
  {
    id: 7,
    name: 'Nicolas Lefebvre',
    email: 'nicolas@example.com',
    role: 'player',
    joinDate: '2023-02-05'
  }
];

// Récupérer tous les utilisateurs
export const getAllUsers = async () => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 500));
  return [...mockUsers];
};

// Récupérer les utilisateurs par rôle
export const getUsersByRole = async (role) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 400));
  return mockUsers.filter(user => user.role === role);
};

// Récupérer un utilisateur par son ID
export const getUserById = async (id) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 300));
  
  const userId = parseInt(id, 10);
  const user = mockUsers.find(u => u.id === userId);
  
  if (!user) {
    throw new Error('Utilisateur non trouvé');
  }
  
  return { ...user };
};

// Rechercher des utilisateurs
export const searchUsers = async (searchTerm) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 500));
  
  const term = searchTerm.toLowerCase();
  return mockUsers.filter(user => 
    user.name.toLowerCase().includes(term) || 
    user.email.toLowerCase().includes(term)
  );
}; 