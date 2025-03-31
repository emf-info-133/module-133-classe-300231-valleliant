import React from 'react';

function App() {
  return (
    <div className="min-h-screen bg-gray-800 flex flex-col justify-center items-center">
      <header className="py-6">
        <h1 className="text-4xl font-bold text-white">Client 2</h1>
      </header>
      <main className="flex-grow flex flex-col justify-center items-center">
        <div className="bg-gray-700 p-8 rounded-lg shadow-md">
          <p className="text-xl text-gray-100 mb-4">
            Bienvenue sur l'application Client 2
          </p>
          <button className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 transition duration-300">
            Démarrer
          </button>
        </div>
      </main>
    </div>
  );
}

export default App; 