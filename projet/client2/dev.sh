#!/bin/bash

# Script de développement pour client2
echo "Démarrage du serveur de développement pour client2..."
export BROWSER=none
export WDS_SOCKET_HOST=0.0.0.0
export NODE_ENV=development

# Vérifier si npx est disponible
if command -v npx &> /dev/null; then
    npx react-scripts start
else
    # Fallback - utiliser directement le script dans node_modules
    node ./node_modules/react-scripts/bin/react-scripts.js start
fi 