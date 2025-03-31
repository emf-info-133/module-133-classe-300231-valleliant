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

## Démarrage

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
