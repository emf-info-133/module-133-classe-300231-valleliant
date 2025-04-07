import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAdmin } from '../context/AdminContext';
import '../styles/AdminLogin.css';

const AdminLogin = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [localError, setLocalError] = useState('');
  const { login, isAuthenticated, loading, error } = useAdmin();
  const navigate = useNavigate();
  
  // Rediriger si déjà authentifié
  useEffect(() => {
    if (isAuthenticated()) {
      navigate('/admin/dashboard');
    }
  }, [isAuthenticated, navigate]);
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLocalError('');
    
    // Validation simple côté client
    if (!email || !password) {
      setLocalError('Veuillez remplir tous les champs');
      return;
    }
    
    try {
      await login({ email, password });
      navigate('/admin/dashboard');
    } catch (err) {
      // L'erreur est déjà gérée dans le contexte
      console.error('Erreur de connexion:', err);
    }
  };
  
  return (
    <div className="admin-login-container">
      <div className="admin-login-card">
        <h1 className="admin-login-title">Connexion Administrateur</h1>
        
        {(error || localError) && (
          <div className="admin-login-error">
            {localError || error}
          </div>
        )}
        
        <form className="admin-login-form" onSubmit={handleSubmit}>
          <div className="admin-login-form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Entrez votre email"
              required
              disabled={loading}
            />
          </div>
          
          <div className="admin-login-form-group">
            <label htmlFor="password">Mot de passe</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Entrez votre mot de passe"
              required
              disabled={loading}
            />
          </div>
          
          <button 
            type="submit" 
            className="admin-login-button"
            disabled={loading}
          >
            {loading ? 'Connexion en cours...' : 'Se connecter'}
          </button>
        </form>
        
        <div className="admin-login-help">
          <p>Identifiants de test:</p>
          <p>Email: admin@tournois.com</p>
          <p>Password: admin123</p>
        </div>
      </div>
    </div>
  );
};

export default AdminLogin; 