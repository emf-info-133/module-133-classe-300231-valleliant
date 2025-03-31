# Service REST 1

Service REST développé avec Spring Boot.

## Fonctionnalités

- API REST pour gérer des ressources
- Persistance des données avec H2 (base de données en mémoire)
- Documentation des endpoints avec Swagger

## Développement

```bash
mvn spring-boot:run
```

## Endpoints

- `GET /api/resource` - Liste toutes les ressources
- `POST /api/resource` - Crée une nouvelle ressource
- `GET /api/resource/{id}` - Récupère une ressource par ID
- `PUT /api/resource/{id}` - Met à jour une ressource
- `DELETE /api/resource/{id}` - Supprime une ressource

## Docker

Pour construire l'image Docker:

```bash
docker build -t service-rest1 .
```

Pour exécuter le conteneur:

```bash
docker run -p 8081:8080 service-rest1
```
