import React from 'react';

function App() {
  return (
    <div className="min-h-screen bg-gray-100 flex flex-col justify-center items-center">
      <header className="py-6">
        <h1 className="text-4xl font-bold text-gray-800">Client 1</h1>
      </header>
      <main className="flex-grow flex flex-col justify-center items-center">
        <div className="bg-white p-8 rounded-lg shadow-md">
          <p className="text-xl text-gray-700 mb-4">
            Bienvenue sur l'application Client 1
          </p>
          <button className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition duration-300">
            Commencer
          </button>
        </div>
      </main>
    </div>
  );
}

export default App; 