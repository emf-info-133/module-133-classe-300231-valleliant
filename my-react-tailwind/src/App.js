import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import AppRoutes from './routes'; // Importer les définitions de routes
import Navbar from './components/layout/Navbar'; // Créer ce composant ensuite
import Footer from './components/layout/Footer'; // Créer ce composant ensuite

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="flex flex-col min-h-screen bg-gray-900 text-gray-100">
          <Navbar />
          <main className="flex-grow container mx-auto px-4 py-8">
            <AppRoutes />
          </main>
          <Footer />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App; 