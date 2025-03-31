# Projet de Microservices

Ce projet implémente une architecture de microservices avec :

- Une API Gateway (Spring Boot)
- Deux services REST (Spring Boot)
- Deux clients React avec Tailwind CSS

## Structure du projet

```
projet/
├── apiGateway/         # API Gateway Spring Boot
├── client1/            # Client React 1
├── client2/            # Client React 2
├── serviceRest1/       # Service REST 1
├── serviceRest2/       # Service REST 2
└── docker-compose.yml  # Configuration Docker Compose
```

## Développement local

Pour les clients React :

```bash
# Client 1
cd client1
npm install
npm run dev
```

Sous WSL, si vous rencontrez des problèmes avec la commande ci-dessus, utilisez :

```bash
# Client 1
cd client1
npm run dev:wsl
# ou directement
./dev.sh
```

De même pour client2 :

```bash
# Client 2
cd client2
npm install
npm run dev
# Ou avec WSL si nécessaire
npm run dev:wsl
```

Pour les services Spring Boot :

```bash
# API Gateway
cd apiGateway
mvn spring-boot:run

# Service REST 1
cd serviceRest1
mvn spring-boot:run

# Service REST 2
cd serviceRest2
mvn spring-boot:run
```

## Démarrage avec Docker

Pour lancer l'ensemble des services :

```bash
docker-compose up -d
```

## Accès

- Client 1 : http://localhost:3000
- Client 2 : http://localhost:3001
- API Gateway : http://localhost:8080
- Service REST 1 : http://localhost:8081
- Service REST 2 : http://localhost:8082
