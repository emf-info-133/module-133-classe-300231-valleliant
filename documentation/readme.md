# Documentation du Projet

## Architecture

Ce projet est construit selon une architecture de microservices comprenant:

1. **API Gateway** - Point d'entrée unique pour toutes les requêtes
2. **Services REST** - Services métier indépendants
3. **Clients Web** - Applications frontales en React avec Tailwind CSS

## Diagramme d'architecture

```
┌─────────┐     ┌─────────┐
│ Client 1 │     │ Client 2 │
└────┬────┘     └────┬────┘
     │               │
     └───────┬───────┘
             │
       ┌─────▼─────┐
       │ API Gateway │
       └─────┬─────┘
      ┌──────┴──────┐
┌─────▼─────┐   ┌─────▼─────┐
│Service REST1│   │Service REST2│
└───────────┘   └───────────┘
```

## Technologies utilisées

- **Frontend** : React, Tailwind CSS
- **Backend** : Spring Boot, Java
- **Conteneurisation** : Docker, Docker Compose
