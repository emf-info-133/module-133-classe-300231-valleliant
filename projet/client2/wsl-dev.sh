#!/bin/bash

# Script de développement pour client2 dans WSL
echo "Démarrage du serveur de développement pour client2 avec correction WSL..."
export BROWSER=none
export WDS_SOCKET_HOST=0.0.0.0
export NODE_ENV=development
export REACT_APP_API_URL=http://localhost:8080/api

# Désactiver la vérification de manière explicite des chemins Windows
export SKIP_PREFLIGHT_CHECK=true

# Exécuter notre script de correction avec node
if command -v node &> /dev/null; then
    echo "Utilisation de la correction WSL..."
    node wsl-fix.js start
else
    echo "Node.js n'est pas disponible. Veuillez l'installer."
    exit 1
fi 