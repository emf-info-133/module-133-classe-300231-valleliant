const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export const getAllTournaments = async () => {
  const token = localStorage.getItem('token');
  
  try {
    const response = await fetch(`${API_URL}/tournaments`, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error('Impossible de récupérer les tournois');
    }

    return await response.json();
  } catch (error) {
    throw error;
  }
};

export const createTeam = async (tournamentId, teamName) => {
  const token = localStorage.getItem('token');
  
  try {
    const response = await fetch(`${API_URL}/teams`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify({ 
        name: teamName,
        tournamentId
      }),
    });

    if (!response.ok) {
      throw new Error('Impossible de créer l\'équipe');
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