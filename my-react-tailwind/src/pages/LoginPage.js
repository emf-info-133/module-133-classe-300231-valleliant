import React, { useState } from 'react';
import { useNavigate, Link, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const auth = useAuth();

  // Rediriger si déjà connecté
  if (auth.isAuthenticated) {
      const from = location.state?.from?.pathname || (auth.isAdmin ? "/admin/dashboard" : "/dashboard");
      navigate(from, { replace: true });
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await auth.login(email, password);
      // La redirection est gérée par le contexte ou un effet après la mise à jour de l'état
      // Pour plus de certitude, on peut naviguer ici après succès
      const from = location.state?.from?.pathname || (auth.isAdmin ? "/admin/dashboard" : "/dashboard");
      navigate(from || '/', { replace: true });
    } catch (err) {
      setError(err.response?.data || 'Erreur de connexion. Veuillez vérifier vos identifiants.');
      console.error("Erreur de connexion:", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-[calc(100vh-200px)]"> {/* Ajustez la hauteur si nécessaire */}
      <div className="w-full max-w-md p-8 space-y-6 bg-gray-800 rounded-lg shadow-lg">
        <h2 className="text-3xl font-bold text-center text-indigo-400">Connexion</h2>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-300 mb-1">
              Adresse Email
            </label>
            <input
              id="email"
              name="email"
              type="email"
              autoComplete="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="votreadresse@email.com"
              className="w-full"
            />
          </div>
          <div>
            <label
              htmlFor="password"
              className="block text-sm font-medium text-gray-300 mb-1"
            >
              Mot de passe
            </label>
            <input
              id="password"
              name="password"
              type="password"
              autoComplete="current-password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              className="w-full"
            />
          </div>

          {error && (
              <div className="text-red-500 text-sm text-center p-3 bg-red-900 border border-red-700 rounded">
                  {error}
              </div>
          )}

          <div>
            <button
              type="submit"
              disabled={loading}
              className="w-full btn-primary py-2 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Connexion en cours...' : 'Se connecter'}
            </button>
          </div>
        </form>
        <p className="text-sm text-center text-gray-400">
          Pas encore de compte ?{' '}
          <Link to="/register" className="font-medium text-indigo-400 hover:text-indigo-300">
            Inscrivez-vous
          </Link>
        </p>
      </div>
    </div>
  );
};

export default LoginPage; 