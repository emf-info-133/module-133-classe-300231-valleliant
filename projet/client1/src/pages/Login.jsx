import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';

const Login = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  
  const { login, register } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      if (isLogin) {
        await login(formData.email, formData.password);
      } else {
        await register(formData.name, formData.email, formData.password);
      }
      navigate('/tournaments');
    } catch (err) {
      setError(err.message || 'Une erreur est survenue');
    } finally {
      setLoading(false);
    }
  };

  // Fonction pour pré-remplir le formulaire avec un utilisateur de démo
  const fillDemoUser = () => {
    setFormData({
      name: '',
      email: 'test@example.com',
      password: 'password123'
    });
  };
  
  const fillAdminUser = () => {
    setFormData({
      name: '',
      email: 'admin@example.com',
      password: 'admin123'
    });
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <h2 className="text-2xl font-bold mb-6 text-center">
          {isLogin ? 'Connexion' : 'Créer un compte'}
        </h2>
        
        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            {error}
          </div>
        )}
        
        {isLogin && (
          <div className="bg-blue-100 border border-blue-400 text-blue-700 px-4 py-3 rounded mb-4">
            <p className="font-bold mb-1">Comptes de démonstration :</p>
            <p className="mb-1">
              <button
                className="text-blue-600 underline"
                onClick={fillDemoUser}
                type="button"
              >
                Utilisateur : test@example.com / password123
              </button>
            </p>
            <p>
              <button
                className="text-blue-600 underline"
                onClick={fillAdminUser}
                type="button"
              >
                Admin : admin@example.com / admin123
              </button>
            </p>
            <p className="mt-2 text-xs">Cliquez sur un compte pour pré-remplir le formulaire</p>
          </div>
        )}
        
        <form onSubmit={handleSubmit}>
          {!isLogin && (
            <div className="mb-4">
              <label className="block text-gray-700 mb-2" htmlFor="name">
                Nom
              </label>
              <input
                id="name"
                type="text"
                name="name"
                className="w-full p-2 border rounded"
                value={formData.name}
                onChange={handleChange}
                required={!isLogin}
              />
            </div>
          )}
          
          <div className="mb-4">
            <label className="block text-gray-700 mb-2" htmlFor="email">
              Email
            </label>
            <input
              id="email"
              type="email"
              name="email"
              className="w-full p-2 border rounded"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          
          <div className="mb-6">
            <label className="block text-gray-700 mb-2" htmlFor="password">
              Mot de passe
            </label>
            <input
              id="password"
              type="password"
              name="password"
              className="w-full p-2 border rounded"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>
          
          <button
            type="submit"
            className="w-full bg-blue-500 text-white p-2 rounded hover:bg-blue-600 transition"
            disabled={loading}
          >
            {loading ? 'Chargement...' : isLogin ? 'Se connecter' : 'S\'inscrire'}
          </button>
        </form>
        
        <div className="mt-4 text-center">
          <button
            className="text-blue-500 hover:underline"
            onClick={() => setIsLogin(!isLogin)}
          >
            {isLogin ? 'Créer un compte' : 'Déjà inscrit ? Se connecter'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default Login; 