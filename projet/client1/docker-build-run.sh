#!/bin/bash

# Script pour construire et exécuter l'application client1 dans Docker
echo "========== Construction et exécution de l'application client1 avec Docker =========="

# Nom de l'image et du conteneur
IMAGE_NAME="client1-app"
CONTAINER_NAME="client1-container"

# Construire l'application React
echo "Construction de l'application React..."
./wsl-build.sh

# Vérifier si la construction a réussi
if [ $? -ne 0 ]; then
    echo "Erreur lors de la construction de l'application React."
    exit 1
fi

# Arrêter et supprimer le conteneur existant s'il existe
echo "Nettoyage des conteneurs existants..."
docker stop $CONTAINER_NAME 2>/dev/null || true
docker rm $CONTAINER_NAME 2>/dev/null || true

# Construire l'image Docker
echo "Construction de l'image Docker..."
docker build -t $IMAGE_NAME .

# Vérifier si la construction a réussi
if [ $? -ne 0 ]; then
    echo "Erreur lors de la construction de l'image Docker."
    exit 1
fi

# Exécuter le conteneur
echo "Lancement du conteneur..."
docker run -d --name $CONTAINER_NAME -p 3000:80 --add-host=host.docker.internal:host-gateway $IMAGE_NAME

# Vérifier si le conteneur est en cours d'exécution
if [ $? -eq 0 ]; then
    echo "====================================================================="
    echo "L'application client1 est maintenant accessible à l'adresse:"
    echo "http://localhost:3000"
    echo "====================================================================="
else
    echo "Erreur lors du lancement du conteneur."
    exit 1
fi 