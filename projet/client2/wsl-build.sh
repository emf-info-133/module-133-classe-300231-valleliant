#!/bin/bash

# Script de build pour client2 dans WSL
echo "Construction de la version de production pour client2..."
export SKIP_PREFLIGHT_CHECK=true
export REACT_APP_API_URL=http://localhost:8080/api

# Fonction pour nettoyer les répertoires
clean_build() {
    echo "Nettoyage des répertoires précédents..."
    rm -rf build
}

# Nettoyer l'environnement
clean_build

# Exécuter le build
if command -v npx &> /dev/null; then
    echo "Utilisation de npx..."
    npx react-scripts build
else
    echo "Utilisation directe du script de build..."
    node ./node_modules/react-scripts/bin/react-scripts.js build
fi

echo "Build terminé ! Les fichiers sont disponibles dans le répertoire 'build'." 