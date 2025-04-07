const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Utilisateurs fictifs pour la démonstration
const mockUsers = [
  {
    id: 1,
    name: 'Utilisateur Test',
    email: 'test@example.com',
    password: 'password123'
  },
  {
    id: 2,
    name: 'Admin',
    email: 'admin@example.com',
    password: 'admin123'
  }
];

// Version fictive de l'authentification pour les démonstrations
export const loginUser = async (email, password) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 500));

  // Chercher l'utilisateur dans notre liste fictive
  const user = mockUsers.find(u => u.email === email);

  // Vérifier l'authentification
  if (!user || user.password !== password) {
    throw new Error('Email ou mot de passe incorrect');
  }

  // Créer un faux token JWT
  const token = 'mock-jwt-token-' + Date.now();
  
  // Créer une réponse similaire à celle d'une API réelle
  return {
    token,
    user: {
      id: user.id,
      name: user.name,
      email: user.email
    }
  };
};

export const registerUser = async (name, email, password) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 500));

  // Vérifier si l'email existe déjà
  if (mockUsers.some(u => u.email === email)) {
    throw new Error('Cet email est déjà utilisé');
  }

  // Créer un nouvel utilisateur fictif
  const newUser = {
    id: mockUsers.length + 1,
    name,
    email,
    password
  };

  // Ajouter à la liste des utilisateurs fictifs
  mockUsers.push(newUser);

  // Créer un faux token JWT
  const token = 'mock-jwt-token-' + Date.now();
  
  return {
    token,
    user: {
      id: newUser.id,
      name: newUser.name,
      email: newUser.email
    }
  };
};

export const getUser = async (token) => {
  // Simuler une latence réseau
  await new Promise(resolve => setTimeout(resolve, 300));

  // Vérifier si le token est valide (dans un cas réel, on déchiffrerait le JWT)
  if (!token || !token.startsWith('mock-jwt-token-')) {
    throw new Error('Session expirée');
  }

  // Pour la démo, on retourne l'utilisateur avec l'ID 1
  const user = mockUsers[0];
  
  return {
    id: user.id,
    name: user.name,
    email: user.email
  };
}; 