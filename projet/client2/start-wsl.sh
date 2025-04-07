#!/bin/bash

# Script pour démarrer le client2 en mode développement dans WSL
# avec gestion des erreurs courantes et des options d'installation de dépendances

# Fonction pour afficher les messages d'aide
show_help() {
    echo "Utilisation: ./start-wsl.sh [options]"
    echo "Options:"
    echo "  -i, --install   Installer les dépendances npm avant de démarrer"
    echo "  -h, --help      Afficher cette aide"
}

# Traitement des arguments
INSTALL_DEPS=false

for arg in "$@"; do
    case $arg in
        -i|--install)
            INSTALL_DEPS=true
            shift
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
    esac
done

# Installer les dépendances si demandé
if [ "$INSTALL_DEPS" = true ]; then
    echo "Installation des dépendances npm..."
    npm install
fi

# Démarrer l'application avec les adaptations WSL
chmod +x ./wsl-dev.sh
./wsl-dev.sh 