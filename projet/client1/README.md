# Client 1 - Application de Gestion des Tournois

Cette application React permet aux utilisateurs de se connecter, créer des comptes, voir les tournois disponibles, créer des équipes et rejoindre des équipes existantes.

## Fonctionnalités

- Authentification (connexion/création de compte)
- Liste des tournois disponibles
- Création d'équipes pour un tournoi
- Rejoindre des équipes existantes
- Interface responsive avec Tailwind CSS

## Prérequis

- Node.js (v14 ou supérieur)
- npm ou yarn
- Docker (optionnel, pour la conteneurisation)

## Configuration pour Windows WSL

Cette application peut être exécutée dans un environnement Windows WSL (Windows Subsystem for Linux). Nous avons créé plusieurs scripts pour faciliter le développement et le déploiement dans cet environnement.

### Scripts disponibles

- `npm run start-wsl` : Démarre l'application en mode développement avec les corrections WSL
- `npm run wsl-build` : Construit l'application pour la production avec les corrections WSL
- `./docker-build-run.sh` : Construit et exécute l'application dans un conteneur Docker

### Installation et démarrage

1. Cloner le dépôt
2. Naviguer vers le répertoire du projet
3. Installer les dépendances :
   ```
   npm install
   ```
4. Démarrer l'application en développement :
   ```
   npm run start-wsl
   ```
   Ou en utilisant Docker :
   ```
   ./docker-build-run.sh
   ```

## Variables d'environnement

- `REACT_APP_API_URL` : URL de l'API backend (par défaut: http://localhost:8080/api)
- `SKIP_PREFLIGHT_CHECK` : Désactive certaines vérifications de compatibilité (utile dans WSL)
- `CHOKIDAR_USEPOLLING` : Active le polling pour la détection des changements de fichiers (utile dans WSL)

## Structure du projet

```
src/
├── components/         # Composants réutilisables
│   ├── auth/           # Composants liés à l'authentification
│   ├── layout/         # Composants de mise en page
│   ├── tournaments/    # Composants liés aux tournois
│   └── teams/          # Composants liés aux équipes
├── contexts/           # Contextes React pour la gestion d'état
├── pages/              # Pages principales de l'application
├── services/           # Services pour les appels API
├── styles/             # Styles CSS et configuration Tailwind
└── App.js              # Composant principal
```

## Docker

L'application peut être conteneurisée à l'aide de Docker. Le `Dockerfile` inclus crée une image optimisée en deux étapes :
1. Une étape de build avec Node.js pour compiler l'application
2. Une étape de production avec Nginx pour servir l'application de manière efficace

Pour construire et exécuter l'image Docker :
```
./docker-build-run.sh
```

## Résolution des problèmes WSL

Si vous rencontrez des problèmes lors de l'exécution dans WSL, essayez ces solutions :

1. Utiliser les scripts WSL fournis : `start-wsl.sh` ou `wsl-build.sh`
2. Ajouter `SKIP_PREFLIGHT_CHECK=true` à vos variables d'environnement
3. Activer `CHOKIDAR_USEPOLLING=true` pour la détection des changements de fichiers
4. Si les problèmes persistent, essayer d'utiliser Docker avec `docker-build-run.sh` 