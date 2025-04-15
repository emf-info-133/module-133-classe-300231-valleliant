import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext'; // Correction du chemin d'importation

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  // Si context est null initialement à cause du chargement, 
  // on pourrait vouloir retourner un état de chargement ici ou gérer dans AuthContext.
  // Pour l'instant, on retourne le contexte tel quel.
  return context;
}; 