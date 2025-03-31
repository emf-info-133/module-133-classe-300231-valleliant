# API Gateway

API Gateway basée sur Spring Cloud Gateway pour le routage des requêtes vers les microservices.

## Fonctionnalités

- Routage dynamique des requêtes
- Point d'entrée unique pour l'application
- Gestion des routes vers les différents microservices

## Développement

```bash
mvn spring-boot:run
```

## Endpoints

- `/service1/**` - Redirige vers le Service REST 1
- `/service2/**` - Redirige vers le Service REST 2

## Docker

Pour construire l'image Docker:

```bash
docker build -t api-gateway .
```

Pour exécuter le conteneur:

```bash
docker run -p 8080:8080 api-gateway
```
