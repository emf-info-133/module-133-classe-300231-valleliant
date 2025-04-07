const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export const getTeamsByTournament = async (tournamentId) => {
  const token = localStorage.getItem('token');
  
  try {
    const response = await fetch(`${API_URL}/teams/tournament/${tournamentId}`, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error('Impossible de récupérer les équipes');
    }

    return await response.json();
  } catch (error) {
    throw error;
  }
};

export const getTournamentById = async (tournamentId) => {
  const token = localStorage.getItem('token');
  
  try {
    const response = await fetch(`${API_URL}/tournaments/${tournamentId}`, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error('Impossible de récupérer le tournoi');
    }

    return await response.json();
  } catch (error) {
    throw error;
  }
};

export const joinTeam = async (teamId) => {
  const token = localStorage.getItem('token');
  
  try {
    const response = await fetch(`${API_URL}/teams/${teamId}/join`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error('Impossible de rejoindre l\'équipe');
    }

    return await response.json();
  } catch (error) {
    throw error;
  }
}; 