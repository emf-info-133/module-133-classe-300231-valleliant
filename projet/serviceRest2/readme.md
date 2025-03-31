# Service REST 2

Service REST développé avec Spring Boot.

## Fonctionnalités

- API REST pour gérer des événements
- Persistance des données avec H2 (base de données en mémoire)
- Documentation des endpoints avec Swagger

## Développement

```bash
mvn spring-boot:run
```

## Endpoints

- `GET /api/events` - Liste tous les événements
- `POST /api/events` - Crée un nouvel événement
- `GET /api/events/{id}` - Récupère un événement par ID
- `PUT /api/events/{id}` - Met à jour un événement
- `DELETE /api/events/{id}` - Supprime un événement

## Docker

Pour construire l'image Docker:

```bash
docker build -t service-rest2 .
```

Pour exécuter le conteneur:

```bash
docker run -p 8082:8080 service-rest2
```
