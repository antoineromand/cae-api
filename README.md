# 🥗 Pick and Eat API

**Pick and Eat API** est une API REST monolithique modulaire conçue pour gérer les fonctionnalités d'une application de
commande ou de recommandation alimentaire. Elle adopte une architecture **Domain-Driven Design (DDD)** et est déployée
via Docker avec **PostgreSQL** comme base de données et **Dragonfly** comme cache compatible Redis.

## 📖 Table des matières

- [Architecture](#-architecture-modulaire-ddd)
- [Prérequis](#-prérequis)
- [Lancement](#-lancement)
- [Image Docker](#-image-docker)
- [Configuration](#%EF%B8%8F-exemple-de-fichier-env)
- [Authentification](#-authentification--jwt)
- [Infrastructure](#-infrastructure-technique)

## 🧱 Architecture modulaire DDD

L'API est organisée en modules découplés selon les principes du **Domain-Driven Design** :

- **`authentication/`** : Module métier dédié à l'authentification.
- **`shared/`** : Contient les composants partagés (exceptions, types, middlewares, etc.).
- **`api/`** : Module central, point d'entrée de l'application, qui orchestre les modules métiers.

Chaque module métier est autonome, avec sa propre logique métier, ses contrôleurs et ses services. Les noms des modules
sont simples et sans préfixe/suffixe (ex. : `authentication`, pas `auth-module`).

## 📦 Prérequis

- **Docker** et **Docker Compose** installés.
- Accès au registre privé `ghcr.io`.
- Un fichier `.env` configuré à la racine du projet.

## 🚀 Lancement

1. Vérifiez que le fichier `.env` est présent et correctement configuré.
2. Lancez la stack avec la commande suivante :

   ```bash
   docker compose up -d
   ```

Cela déploie :

- L'API (conteneur principal).
- Une instance PostgreSQL.
- Une instance Dragonfly (cache compatible Redis).

## 🐳 Image Docker

L'image de l'API est disponible sur le registre privé :

```bash
docker pull ghcr.io/pick-and-eat-organization/pick-and-eat-api:0.0.1-snapshot
```

⚠️ **Note** : L'accès à l'image est restreint. Assurez-vous d'avoir les droits nécessaires.

## ⚙️ Exemple de fichier .env

Voici un exemple de configuration pour le fichier `.env` :

```env
# API
PROJECT_MODE=development
SERVER_PORT=8080
USE_TESTCONTAINERS=false

# Base de données
DB_USERNAME=admin
DB_PASSWORD=securepassword
DB_URL=jdbc:postgresql://localhost:5432/pickandeat
DB_NAME=pickandeat

# JWT
JWT_SECRET=your_jwt_secret_key
EXPIRATION_ACCESS_TOKEN=15m
EXPIRATION_REFRESH_TOKEN=7d

# Cache
REDIS_HOST=localhost
REDIS_PORT=6379

# Docker Compose
DOCKER_DB_PORTS=5432:5432
```

## 🔐 Authentification – JWT

L'API utilise le standard **JWT** avec deux types de tokens :

- **Access Token** : Token de courte durée pour les requêtes authentifiées.
- **Refresh Token** : Token de longue durée pour renouveler les access tokens.

### Variables .env pour JWT

- `JWT_SECRET` : Clé secrète pour signer les tokens.
- `EXPIRATION_ACCESS_TOKEN` : Durée de vie de l'access token (ex. : `15m` pour 15 minutes).
- `EXPIRATION_REFRESH_TOKEN` : Durée de vie du refresh token (ex. : `7d` pour 7 jours).

## 🗃 Infrastructure technique

- **Base de données** : PostgreSQL
- **Cache** : Dragonfly (compatible Redis)
- **Architecture** : Monolithe modulaire
- **Langage et framework** : Java 24 / Spring