import React, { useEffect } from 'react';
import ReactDOM from 'react-dom';

const Modal = ({ title, children, onClose }) => {
  // Gérer la fermeture avec la touche Échap
  useEffect(() => {
    const handleEsc = (event) => {
      if (event.keyCode === 27) {
        onClose();
      }
    };
    window.addEventListener('keydown', handleEsc);

    // Nettoyer l'écouteur d'événement au démontage
    return () => {
      window.removeEventListener('keydown', handleEsc);
    };
  }, [onClose]);

  // Utiliser un portail pour rendre la modale en dehors de la hiérarchie DOM parente
  return ReactDOM.createPortal(
    <div 
        className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-60 backdrop-blur-sm" 
        onClick={onClose} // Fermer en cliquant sur l'arrière-plan
    >
      <div 
        className="relative w-full max-w-lg p-6 mx-4 bg-gray-800 rounded-lg shadow-xl text-gray-100" 
        onClick={(e) => e.stopPropagation()} // Empêcher la fermeture en cliquant sur le contenu de la modale
      >
        {/* Bouton de fermeture */}
        <button 
          onClick={onClose} 
          className="absolute top-3 right-3 text-gray-400 hover:text-gray-200 transition duration-150"
          aria-label="Fermer"
        >
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12"></path></svg>
        </button>

        {/* Titre de la modale */}
        {title && (
          <h3 className="text-xl font-semibold mb-4 text-indigo-400">
            {title}
          </h3>
        )}

        {/* Contenu de la modale */}
        <div className="modal-content">
          {children}
        </div>
      </div>
    </div>,
    document.body // Cible du portail (ajoute la modale à la fin du body)
  );
};

export default Modal; 