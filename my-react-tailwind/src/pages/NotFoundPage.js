import React from 'react';
import { Link } from 'react-router-dom';

const NotFoundPage = () => {
  return (
    <div className="text-center py-16">
      <h1 className="text-6xl font-bold text-indigo-400 mb-4">404</h1>
      <h2 className="text-3xl font-semibold text-gray-300 mb-6">Page non trouvée</h2>
      <p className="text-lg text-gray-400 mb-8">
        Oups ! La page que vous recherchez n'existe pas ou a été déplacée.
      </p>
      <Link 
        to="/" 
        className="btn-primary px-6 py-3 text-lg"
      >
        Retour à l'accueil
      </Link>
    </div>
  );
};

export default NotFoundPage; 