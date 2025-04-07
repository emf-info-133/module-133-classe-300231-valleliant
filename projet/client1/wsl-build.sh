#!/bin/bash

# Script de build pour client1 dans WSL
echo "Construction de l'application client1 avec correction WSL..."
export SKIP_PREFLIGHT_CHECK=true
export REACT_APP_API_URL=http://localhost:8080/api
export NODE_ENV=production

# Nettoyer le répertoire de build précédent s'il existe
if [ -d "build" ]; then
  echo "Nettoyage du répertoire build précédent..."
  rm -rf build
fi

# Exécuter le build
if command -v node &> /dev/null; then
    echo "Démarrage du build..."
    # Utiliser directement react-scripts depuis node_modules
    ./node_modules/.bin/react-scripts build
else
    echo "Node.js n'est pas disponible. Veuillez l'installer."
    exit 1
fi

echo "Build terminé. Les fichiers se trouvent dans le répertoire 'build'." 