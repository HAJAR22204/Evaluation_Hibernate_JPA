# Évaluation JPA / Hibernate — Trois Applications Java

> Réalisation de trois applications Java de persistance avec **JPA / Hibernate** et **Maven**, couvrant la gestion de stock, la gestion de projets et la gestion de l'état civil.

---

## Structure du dépôt

```
Evaluation/
Exercice 1 — Application de gestion de stock/
└── gestion_stock/
    ├── src/main/java/ma/projet/
    │   ├── classes/         # Entités JPA : Categorie, Produit, Commande, LigneCommandeProduit
    │   ├── dao/             # Interface générique IDao<T>
    │   ├── service/         # CategorieService, ProduitService, CommandeService, LigneCommandeService
    │   ├── util/            # HibernateUtil
    │   └── App.java         # Programme de test principal
    └── src/main/resources/META-INF/persistence.xml

```

```
Exercice 2 — Application de gestion de projets/
└── gestion_projets/
    ├── src/main/java/ma/projet/
    │   ├── classes/         # Entités JPA : Projet, Employe, Tache, EmployeTache
    │   ├── dao/             # Interface générique IDao<T>
    │   ├── service/         # ProjetService, EmployeService, TacheService, EmployeTacheService
    │   ├── util/            # HibernateUtil
    │   └── App.java         # Programme de test principal
    └── src/main/resources/META-INF/persistence.xml
```


```
Exercice 3 — Application de gestion de l'état civil/
    └── gestion_etat_civil/
    ├── src/main/java/ma/projet/
    │   ├── beans/           # Entités JPA : Personne (abstract), Homme, Femme, Mariage
    │   ├── dao/             # Interface générique IDao<T>
    │   ├── service/         # HommeService, FemmeService, MariageService
    │   ├── util/            # HibernateUtil
    │   └── App.java         # Programme de test principal
    └── src/main/resources/hibernate.cfg.xml
```

---


## Exercice 1 — Application de gestion de stock

### Contexte

Application de gestion du stock d'un magasin de produits.

### Diagramme de classes

<img width="1004" height="562" alt="image" src="https://github.com/user-attachments/assets/5623cee2-b191-4a21-8f72-9eeda7eab190" />


### Couche Persistance

- Package : `ma.projet.classes`
- Configuration JPA : `META-INF/persistence.xml` (H2 in-memory)
- `HibernateUtil` dans `ma.projet.util` — gère l'`EntityManagerFactory` et expose `getEntityManager()`

### Couche Service

Interface générique `IDao<T>` (`ma.projet.dao`) définissant `create`, `delete`, `update`, `findById`, `findAll`.

Services implémentés dans `ma.projet.service` :

| Service | Méthodes notables |
|---|---|
| `CategorieService` | CRUD complet |
| `ProduitService` | CRUD + `findByCategorie`, `findProduitsCommandesEntreDates`, `afficherProduitsParCommande`, `findProduitsPrixSuperieur100` (NamedQuery) |
| `CommandeService` | CRUD complet |
| `LigneCommandeService` | CRUD complet |

La requête nommée `Produit.prixSuperieur100` est déclarée directement sur l'entité `Produit` via `@NamedQuery`.

### Programme de test (`App.java`)

1. Initialisation : 2 catégories, 5 produits, 2 commandes, 5 lignes de commande
2. Liste de tous les produits
3. Produits filtrés par catégorie
4. Produits commandés entre deux dates
5. Détail d'une commande (format : Référence / Prix / Quantité)
6. Produits dont le prix est supérieur à 100 DH

### Base de données

H2 in-memory — la base est créée et détruite à chaque exécution (`create-drop`).

---

## Exercice 2 — Application de gestion de projets

### Contexte

Suivi du temps passé sur les projets dans un bureau d'études et calcul des coûts.

### Diagramme de classes

<img width="450" height="622" alt="image" src="https://github.com/user-attachments/assets/a231fc37-5b1c-48e9-b5f8-20f92cf40279" />


### Couche Persistance

- Package : `ma.projet.classes`
- Configuration JPA : `META-INF/persistence.xml` (H2 in-memory)
- `HibernateUtil` dans `ma.projet.util` — expose `getEntityManagerFactory()`

### Couche Service

Interface générique `IDao<T>` (`ma.projet.dao`) : `create`, `update`, `delete`, `findById`, `findAll`.

Services implémentés dans `ma.projet.service` :

| Service | Méthodes notables |
|---|---|
| `ProjetService` | CRUD + `getTachesPlanifiees(projetId)` + `afficherTachesRealisees(projet)` |
| `EmployeService` | CRUD + `getTachesRealisees(employeId)` + `getProjetsGeres(employeId)` |
| `TacheService` | CRUD + `getTachesParPrixSuperieur(prix)` (NamedQuery) + `getTachesRealiseeEntreDates(d1, d2)` (NamedQuery sur `EmployeTache`) |
| `EmployeTacheService` | CRUD complet |

Requêtes nommées utilisées :
- `Tache.findByPrixSuperieur` — sur l'entité `Tache`
- `EmployeTache.findRealiseeEntreDates` — sur l'entité `EmployeTache`

### Programme de test (`App.java`)

1. Création de 3 employés, 2 projets, 5 tâches, 5 affectations employé-tâche
2. Affichage des tâches réalisées pour un projet (avec dates réelles)
3. Liste des tâches planifiées d'un projet
4. Tâches réalisées par un employé donné
5. Projets gérés par un employé donné (rôle chef de projet)
6. Tâches dont le prix est supérieur à 1000 DH
7. Tâches réalisées entre deux dates
8. Liste de tous les employés

### Base de données

H2 in-memory — la base est créée et détruite à chaque exécution (`create-drop`).

---

## Exercice 3 — Application de gestion de l'état civil

### Contexte

Gestion des citoyens et de leurs relations matrimoniales dans une province.

### Diagramme de classes

<img width="539" height="435" alt="image" src="https://github.com/user-attachments/assets/69b76097-3d75-4ad4-804f-efc999a90f43" />


### Couche Persistance

- Package : `ma.projet.beans`
- Stratégie d'héritage : `InheritanceType.JOINED` avec `@DiscriminatorColumn`
- Configuration : `hibernate.cfg.xml` (MySQL 8, base `EC`)
- `HibernateUtil` dans `ma.projet.util` — expose `getSession()` via `SessionFactory`

### Couche Service

Interface générique `IDao<T>` (`ma.projet.dao`) : `create`, `delete`, `update`, `findById`, `findAll`.

Services implémentés dans `ma.projet.service` :

| Service | Méthodes notables |
|---|---|
| `HommeService` | CRUD + `getEpousesEntreDates(homme, d1, d2)` + `afficherMariagesDetailles(homme)` |
| `FemmeService` | CRUD + `getFemmeLaPlusAgee()` + `getNbrEnfantsEntreDates(femme, d1, d2)` (requête native nommée) + `getFemmesMarieesAuMoinsDeux()` (NamedQuery JPQL) + `getNbrHommesMariesAQuatreFemmes(d1, d2)` (Criteria API) |
| `MariageService` | CRUD complet |

Requêtes nommées utilisées :
- `Femme.nbrEnfantsEntreDates` — `@NamedNativeQuery` SQL native sur `MARIAGE`
- `Femme.femmesMarieesPlusDeDeux` — `@NamedQuery` JPQL avec `GROUP BY` / `HAVING COUNT`

La méthode `getNbrHommesMariesAQuatreFemmes` utilise l'**API Criteria** avec une sous-requête corrélée pour compter les hommes ayant exactement 4 épouses dans une période donnée.

### Programme de test (`App.java`)

1. Création de 10 femmes et 5 hommes
2. Affichage de la liste des femmes
3. Affichage de la femme la plus âgée
4. Épouses d'un homme donné entre deux dates
5. Nombre d'enfants d'une femme entre deux dates (requête native)
6. Femmes mariées au moins deux fois (NamedQuery)
7. Nombre d'hommes mariés à 4 femmes entre deux dates (Criteria API)
8. Mariages détaillés d'un homme (mariages en cours + mariages échoués)

### Base de données

MySQL 8 — la base `EC` doit exister avant l'exécution. Le schéma est généré automatiquement (`hbm2ddl.auto=update`).

<img width="590" height="476" alt="image" src="https://github.com/user-attachments/assets/0451ee30-d0fb-4078-a3a0-bc3592e34992" />

<img width="1100" height="870" alt="image" src="https://github.com/user-attachments/assets/c5ccc23a-b4ee-4151-bdc3-275e2391f153" />

<img width="560" height="715" alt="image" src="https://github.com/user-attachments/assets/c9ab97c8-e84c-4ea0-90d0-c53d89d73c61" />

<img width="575" height="539" alt="image" src="https://github.com/user-attachments/assets/2e3ec590-616f-42b9-bc40-8cdbb2689166" />

<img width="939" height="789" alt="image" src="https://github.com/user-attachments/assets/d00c0511-3ce1-4f1a-b77b-b4893cd9f623" />

