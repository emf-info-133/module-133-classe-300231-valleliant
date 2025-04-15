import React from 'react';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-gray-800 mt-auto">
      <div className="container mx-auto px-4 py-6 text-center text-gray-400 text-sm">
        <p>
          &copy; {currentYear} TournoiApp. Tous droits réservés.
        </p>
        {/* Ajoutez d'autres liens ou informations si nécessaire */}
        {/* <p className="mt-2">
          <a href="/privacy" className="hover:text-gray-300">Politique de confidentialité</a> | 
          <a href="/terms" className="hover:text-gray-300">Conditions d'utilisation</a>
        </p> */}
      </div>
    </footer>
  );
};

export default Footer; 