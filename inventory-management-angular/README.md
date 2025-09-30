# Gestion de Stock - Application Angular

Une application complète de gestion de stock développée avec Angular 20.2.0, intégrée avec un backend Spring Boot pour la gestion des articles, clients, fournisseurs, commandes, mouvements de stock, utilisateurs et rôles.

## 📋 Table des Matières

- [Vue d'ensemble](#vue-densemble)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Architecture du Projet](#architecture-du-projet)
- [Fonctionnalités](#fonctionnalités)
- [Scripts Disponibles](#scripts-disponibles)
- [Technologies Utilisées](#technologies-utilisées)
- [Structure des Dossiers](#structure-des-dossiers)
- [API et Services](#api-et-services)
- [Authentification](#authentification)
- [Déploiement](#déploiement)
- [Développement](#développement)

## 🎯 Vue d'ensemble

Cette application Angular permet de gérer efficacement un système de stock avec les fonctionnalités suivantes :
- Gestion des articles et catégories
- Gestion des clients et fournisseurs
- Gestion des commandes (clients et fournisseurs)
- Suivi des mouvements de stock
- Gestion des utilisateurs et rôles
- Tableau de bord avec statistiques
- Authentification JWT sécurisée

## 📋 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **Node.js** (version 18.x ou supérieure)
- **npm** (version 9.x ou supérieure)
- **Angular CLI** (version 20.2.0)
- **Backend Spring Boot** (inventory-management-backend)
- **Java 17+** pour le backend
- **Base de données** (MySQL/PostgreSQL)

```bash
# Vérifier les versions installées
node --version
npm --version
ng version
```

## 🚀 Installation

1. **Cloner le projet**
```bash
git clone <repository-url>
cd gestiondestocks-angular
```

2. **Installer les dépendances**
```bash
npm install
```

3. **Démarrer le backend Spring Boot**
```bash
# Naviguer vers le dossier backend
cd ../inventory-management-backend
# Démarrer l'application Spring Boot (port 8888)
./mvnw spring-boot:run
```

4. **Générer les API clients (optionnel)**
```bash
npm run gs-api
```

5. **Démarrer l'application Angular**
```bash
npm start
# ou avec proxy pour le développement local
npm run start:local
```

L'application sera accessible sur `http://localhost:4200/`

## ⚙️ Configuration

### Configuration Angular (angular.json)

```json
{
  "projects": {
    "gestiondestocks": {
      "architect": {
        "build": {
          "options": {
            "styles": [
              "src/styles.css",
              "node_modules/bootstrap/dist/css/bootstrap.min.css",
              "node_modules/@fortawesome/fontawesome-free/css/all.css"
            ],
            "scripts": [
              "node_modules/bootstrap/dist/js/bootstrap.js"
            ]
          }
        }
      }
    }
  }
}
```

### Configuration TypeScript (tsconfig.json)

```json
{
  "compilerOptions": {
    "strict": true,
    "target": "ES2022",
    "module": "preserve",
    "experimentalDecorators": true,
    "strictTemplates": true
  }
}
```

### Variables d'Environnement

Créer un fichier `src/environments/environment.ts` :

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8888/gestiondestock/api/v1'
};
```

## 🏗️ Architecture du Projet

### Structure MVC Angular

```
src/app/
├── pages/              # Pages principales de l'application
├── composants/         # Composants réutilisables
├── services/           # Services pour l'API et la logique métier
├── shared/            # Éléments partagés
├── app.routes.ts      # Configuration des routes
└── app.config.ts      # Configuration de l'application
```

### Modèle de Données

L'application gère les entités suivantes :
- **Articles** : Produits avec code, nom, prix, quantité
- **Catégories** : Classification des articles
- **Clients/Fournisseurs** : Partenaires commerciaux
- **Commandes** : Commandes clients et fournisseurs
- **Mouvements de Stock** : Entrées, sorties, corrections
- **Utilisateurs** : Comptes utilisateurs avec rôles
- **Entreprises** : Informations de l'entreprise

## 🎨 Fonctionnalités

### 1. Gestion des Articles
- ✅ Création, modification, suppression d'articles
- ✅ Upload d'images produit
- ✅ Gestion des catégories
- ✅ Suivi des quantités en stock

### 2. Gestion des Partenaires
- ✅ Clients et fournisseurs
- ✅ Informations de contact complètes
- ✅ Historique des commandes

### 3. Gestion des Commandes
- ✅ Commandes clients et fournisseurs
- ✅ Lignes de commande détaillées
- ✅ Calcul automatique des totaux
- ✅ Statuts de commande

### 4. Mouvements de Stock
- ✅ Entrées de stock
- ✅ Sorties de stock
- ✅ Corrections positives/négatives
- ✅ Historique complet

### 5. Gestion des Utilisateurs
- ✅ Création de comptes utilisateurs
- ✅ Système de rôles et permissions
- ✅ Authentification JWT
- ✅ Gestion des profils

### 6. Tableau de Bord
- ✅ Statistiques en temps réel
- ✅ Graphiques et métriques
- ✅ Vue d'ensemble des activités

## 📜 Scripts Disponibles

### Scripts de Développement

```bash
# Démarrer le serveur de développement
npm start
npm run ng serve

# Démarrer avec proxy backend
npm run start:local

# Build de production
npm run build

# Tests unitaires
npm run test

# Build en mode watch
npm run watch
```

### Scripts API

```bash
# Génération complète des API clients
npm run gs-api

# Étapes individuelles
npm run gs-api:create-dist    # Créer les dossiers
npm run gs-api:download       # Télécharger les fichiers
npm run gs-api:extract        # Extraire le JAR
npm run gs-api:openapi-generator-cli  # Générer les clients
```

## 🛠️ Technologies Utilisées

### Frontend
- **Angular 20.2.0** - Framework principal
- **TypeScript 5.9.2** - Langage de programmation
- **Bootstrap 5.3.7** - Framework CSS
- **FontAwesome 7.0.0** - Icônes
- **RxJS 7.8.0** - Programmation réactive

### Outils de Développement
- **Angular CLI 20.2.0** - Outils de développement
- **Karma & Jasmine** - Tests unitaires
- **OpenAPI Generator** - Génération des clients API
- **Prettier** - Formatage du code

### Configuration Prettier

```json
{
  "printWidth": 100,
  "singleQuote": true,
  "overrides": [
    {
      "files": "*.html",
      "options": {
        "parser": "angular"
      }
    }
  ]
}
```

## 📁 Structure des Dossiers

```
gestiondestocks-angular/
├── public/                 # Fichiers statiques
│   ├── favicon.ico
│   ├── product.png
│   └── swagger.json
├── src/
│   ├── app/
│   │   ├── pages/          # Pages de l'application
│   │   │   ├── articles/   # Gestion des articles
│   │   │   ├── categories/ # Gestion des catégories
│   │   │   ├── client/     # Gestion des clients
│   │   │   ├── fournisseur/# Gestion des fournisseurs
│   │   │   ├── mvtstk/     # Mouvements de stock
│   │   │   ├── utilisateur/# Gestion des utilisateurs
│   │   │   ├── roles/      # Gestion des rôles
│   │   │   └── ...
│   │   ├── composants/     # Composants réutilisables
│   │   │   ├── nouveau-clt-frs/
│   │   │   ├── nouvelle-cmd-clt-frs/
│   │   │   └── detail-utilisateur/
│   │   ├── services/       # Services Angular
│   │   │   ├── article/
│   │   │   ├── user/
│   │   │   ├── guard/
│   │   │   └── intercepter/
│   │   └── shared/         # Éléments partagés
│   ├── assets/            # Ressources (images, etc.)
│   ├── environments/      # Configuration d'environnement
│   └── styles.css         # Styles globaux
├── tools/
│   └── swagger/           # Outils de génération API
├── .editorconfig          # Configuration de l'éditeur
├── angular.json           # Configuration Angular
├── package.json           # Dépendances npm
└── tsconfig.json          # Configuration TypeScript
```

## 🔌 API et Services

### Services Principaux

1. **UserService** - Authentification et gestion de session
2. **UtilisateurService** - CRUD utilisateurs
3. **ArticleService** - Gestion des articles
4. **CltfrsService** - Clients et fournisseurs
5. **CmdcltfrsService** - Commandes
6. **CategoryService** - Gestion des catégories
7. **RolesService** - Gestion des rôles
8. **MvtstkService** - Mouvements de stock

### Endpoints Backend

```
Base URL: http://localhost:8888/gestiondestock/api/v1

Authentication:
POST /auth/login
POST /auth/register

Entities:
GET|POST|PUT|DELETE /articles
GET|POST|PUT|DELETE /categories
GET|POST|PUT|DELETE /clients
GET|POST|PUT|DELETE /fournisseurs
GET|POST|PUT|DELETE /commandesclients
GET|POST|PUT|DELETE /commandesfournisseurs
GET|POST|PUT|DELETE /utilisateurs
GET|POST|PUT|DELETE /roles
GET|POST|PUT|DELETE /mvtstk
```

## 🔐 Authentification

### Système JWT

L'application utilise JWT (JSON Web Tokens) pour l'authentification :

1. **Login** : `POST /auth/login` retourne un token JWT
2. **Stockage** : Token stocké dans localStorage
3. **Intercepteur** : Ajout automatique du token aux requêtes
4. **Guard** : Protection des routes avec ApplicationGuard
5. **Expiration** : Vérification automatique de l'expiration

### Guard de Route

```typescript
// ApplicationGuard protège toutes les routes privées
{
  path: '',
  component: PageDashboard,
  canActivate: [ApplicationGuard],
  children: [...]
}
```

## 🚀 Déploiement

### Build de Production

```bash
# Build optimisé pour la production
ng build --configuration production

# Les fichiers sont générés dans dist/
```

### Configuration de Production

```typescript
// src/environments/environment.prod.ts
export const environment = {
  production: true,
  apiUrl: 'https://your-api-domain.com/gestiondestock/api/v1'
};
```

## 👨‍💻 Développement

### Génération de Code

```bash
# Nouveau composant
ng generate component pages/nouvelle-page

# Nouveau service
ng generate service services/nouveau-service

# Nouveau guard
ng generate guard services/guard/nouveau-guard
```

### Conventions de Code

1. **Nommage** : kebab-case pour les fichiers, camelCase pour les variables
2. **Structure** : Un dossier par fonctionnalité
3. **Services** : Wrapper des API générées
4. **Composants** : Logique métier séparée de la présentation
5. **Types** : Utilisation des interfaces TypeScript

### Debugging

```bash
# Mode développement avec source maps
ng serve --source-map

# Analyse du bundle
ng build --stats-json
npx webpack-bundle-analyzer dist/stats.json
```

## 🔧 Résolution de Problèmes

### Problèmes Courants

1. **Erreurs TypeScript** : Redémarrer le backend et régénérer les API
2. **CORS** : Vérifier la configuration CORS du backend
3. **Token expiré** : Se reconnecter à l'application
4. **Build errors** : Nettoyer node_modules et réinstaller

### Commandes Utiles

```bash
# Nettoyer et réinstaller
rm -rf node_modules package-lock.json
npm install

# Régénérer les API clients
npm run gs-api

# Redémarrer en mode propre
ng serve --host 0.0.0.0 --port 4200
```

## 📞 Support

Pour toute question ou problème :
1. Vérifier la documentation du backend Spring Boot
2. Consulter les logs de la console navigateur
3. Vérifier les logs du serveur backend
4. S'assurer que toutes les dépendances sont installées

## 👥 Équipe de Développement

Ce projet a été développé par :
- **Hassan**
- **Vincent** 
- **Loïc**
- **Belvina**
- **Jake**

---

**Version** : 0.0.0  
**Angular** : 20.2.0  
**Node.js** : 18+  
**Dernière mise à jour** : 2024
