#!/bin/bash

# Script complet pour démarrer l'application client1 dans WSL
echo "========== Configuration de l'environnement WSL pour React =========="

# Définir les variables d'environnement
export BROWSER=none
export WDS_SOCKET_HOST=0.0.0.0
export SKIP_PREFLIGHT_CHECK=true
export REACT_APP_API_URL=http://localhost:8080/api
export CHOKIDAR_USEPOLLING=true
export NODE_ENV=development
export DISABLE_ESLINT_PLUGIN=true
export ESLINT_NO_DEV_ERRORS=true

# Vérifier si Node.js est installé
if ! command -v node &> /dev/null; then
    echo "Erreur: Node.js n'est pas installé. Veuillez l'installer pour continuer."
    exit 1
fi

# Vérifier si les dépendances sont installées
if [ ! -d "node_modules" ]; then
    echo "Installation des dépendances..."
    npm install
fi

# Ajouter des corrections pour WSL
echo "Application des correctifs WSL pour React..."

# Démarrer l'application
echo "Démarrage de l'application client1..."
npx react-scripts start 