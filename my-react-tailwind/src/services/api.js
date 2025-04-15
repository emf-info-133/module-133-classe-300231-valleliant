import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080', // URL de votre API Gateway
  withCredentials: true, // Important pour envoyer les cookies de session
});

// Intercepteur pour ajouter des logs ou gérer les erreurs globalement si nécessaire
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('Erreur API:', error.response || error.message);
    // Vous pouvez gérer les erreurs spécifiques ici (ex: 401 non autorisé)
    return Promise.reject(error);
  }
);

// --- Authentification ---
export const login = (email, password) =>
  apiClient.post('/auth/login', { username: email, password }); // Assumant username = email

export const logout = () => apiClient.post('/auth/logout');

export const register = (name, email, password) =>
  apiClient.post('/register', { name, email, password });


// --- Utilisateurs (Users) ---
// NOTE: Basé sur l'analyse, la récupération de tous les utilisateurs est disponible
// mais la création/modification se fait via /register ou par l'utilisateur lui-même.
// Une route pour récupérer l'utilisateur courant serait utile (/api/users/me)
// En attendant, on suppose que le contexte d'authentification gère l'état admin.
export const getAllUsers = () => apiClient.get('/api/users');
export const getUserById = (id) => apiClient.get(`/api/users/${id}`);
// Pas de createUser ici, géré par /register ou potentiellement une route admin non utilisée par le flow standard.
export const updateUser = (id, userData) => apiClient.put(`/api/users/${id}`, userData); // L'API ne permet que l'auto-modification


// --- Équipes (Teams) ---
export const getAllTeams = () => apiClient.get('/api/teams');
export const getTeamById = (id) => apiClient.get(`/api/teams/${id}`);
export const createTeam = (teamData) => apiClient.post('/api/teams', teamData);
export const updateTeam = (id, teamData) => apiClient.put(`/api/teams/${id}`, teamData);
export const deleteTeam = (id) => apiClient.delete(`/api/teams/${id}`);


// --- Tournois (Tournaments) ---
export const getAllTournaments = () => apiClient.get('/api/tournaments');
export const getTournamentById = (id) => apiClient.get(`/api/tournaments/${id}`);
export const createTournament = (tournamentData) => apiClient.post('/api/tournaments', tournamentData); // Admin only
export const updateTournament = (id, tournamentData) => apiClient.put(`/api/tournaments/${id}`, tournamentData); // Admin only
export const deleteTournament = (id) => apiClient.delete(`/api/tournaments/${id}`); // Admin only


// --- Matchs (Matches) ---
export const getAllMatches = () => apiClient.get('/api/matches');
export const getMatchById = (id) => apiClient.get(`/api/matches/${id}`);
export const createMatch = (matchData) => apiClient.post('/api/matches', matchData); // Admin only
export const updateMatch = (id, matchData) => apiClient.put(`/api/matches/${id}`, matchData); // Admin only
export const deleteMatch = (id) => apiClient.delete(`/api/matches/${id}`); // Admin only

// --- Jeux (Games) ---
export const getAllGames = () => apiClient.get('/api/games');
export const getGameById = (id) => apiClient.get(`/api/games/${id}`);

export default apiClient; 