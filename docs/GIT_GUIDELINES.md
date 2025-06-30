# 🛠️ Git & CI Guidelines – Projet Backend API

Ce document décrit les règles de collaboration Git ainsi que la configuration de l'intégration continue (CI) mise en place pour ce projet backend. Bien que le projet soit actuellement maintenu par une seule personne, il est conçu pour être facilement scalable et collaboratif.

---

## 🚀 Branche principale : `develop`

Toutes les nouvelles fonctionnalités doivent être intégrées via des **Pull Requests** (PR) vers la branche `develop`.

---

## 🔄 Pipeline CI – Branche `develop`

Une **pipeline CI** est automatiquement déclenchée sur chaque PR ou push vers `develop`. Elle effectue plusieurs **vérifications** (checks) essentielles :

### ✅ Checks CI

| Étape                     | Description |
|--------------------------|-------------|
| **Linting**              | Utilisation de [Spotless](https://github.com/diffplug/spotless) pour appliquer les règles de formatage Java. |
| **Tests unitaires – modules métier** | Chaque module métier (ex: `authentication`, `order`) doit avoir au minimum **80% de couverture de tests**. |
| **Tests unitaires – module API central** | Ce module, principalement de configuration (DTOs, Swagger, controllers...), requiert **20% de couverture minimale**. |
| **Tests d’intégration**  | Vérifient que les composants (ex: repositories) fonctionnent correctement dans un environnement de test avec base de données dédiée. |
| **Tests fonctionnels**   | Testent des blocs métier complets (ex: use cases) en environnement de test. |
| **Build sans tests**     | Vérifie que le projet peut être compilé sans erreur. |

---

## 🔒 Règles de protection de branche

La branche `develop` est protégée via les règles suivantes :

- ❌ **Pas de push direct** : seuls les merges via PR sont autorisés.
- ✅ **Tous les checks CI doivent être passés** avant de pouvoir merger une PR.

---

## 📦 Pull Requests

### Règles pour les PR sur `develop` :

- Une PR = une fonctionnalité claire ou un correctif ciblé.
- Les tests associés doivent être inclus dans la PR.
- Décrire dans la PR :
    - La fonctionnalité ajoutée ou le bug corrigé.
    - L’impact éventuel sur le reste du système.
- Lint et build doivent passer avant soumission.

---

## 🧪 Environnement de test

Les tests d’intégration et fonctionnels s’exécutent dans un environnement isolé :

- **Base de données de test** initialisée à chaque run
- Utilisation de données fictives
- Nettoyage après chaque test pour éviter les conflits

---

## 💡 Bonnes pratiques

- **Créer une branche par fonctionnalité** à partir de `develop` :
  ```bash
  git checkout -b <ticket_jira>
  ex: git checkout -b api-86
  ```
- **Commits clairs et réguliers**, par exemple :
  ```bash
  feat: add loginUseCase functional tests
  ```
  ou mieux encore :
  ```bash
  feat: add loginUseCase functional tests (authentication)
  ```
  
---

## 📎 Références

- [Documentation Spotless](https://github.com/diffplug/spotless)
- [Règles de couverture recommandées par module](https://martinfowler.com/bliki/TestCoverage.html)


