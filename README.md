
# Modèle Toy case SEIR multi-agent






## Contributeurs
* KABOURI Mouad
* MONJI Said
* SEDDIKI Ayoub
## I-Objectif
L’objectif principal du projet est de développer un modèle multi-agent simple de propagation d'une maladie dans une population. 
## II-Structure du projet
Le projet contient principalement trois packages ``Models``, ``Services``, et ``utils``.
### 1-packages
#### 1-a) Le package Models 
Le package "models" contient des classes (Grille, Cellule, Individual, et Status) qui représentent des modèles de données dans l'application. Ces classes sont conçues pour encapsuler les données et le comportement associés à ces modèles.

#### 1-b) Le package Services
le package "Services" contient des classes qui encapsulent la logique métier de l'application et fournissent des services ou des fonctionnalités spécifiques.

#### 1-c) Le package Utils
Le package "utils" est  utilisé pour regrouper des classes utilitaires qui fournissent des fonctionnalités génériques et des méthodes réutilisables dans une application.

### 2-patron de conception

Dans ce projet, nous avons utilisé plusieurs design patterns pour améliorer la structure et la flexibilité de notre code. Voici une liste des design patterns utilisés :
#### 2-a) Singleton (Création) 
Le design pattern ``Singleton`` est un modèle de conception qui permet de s'assurer qu'une seule instance d'une classe est créée et fournit un point d'accès global à cette instance.

Exemple : L'instance de PRNG, et du service.

#### 2-b) Model en couches (Structure)
Ce patron permet d'organiser une application en différentes couches, chaque couche ayant une responsabilité spécifique. Chaque couche communique avec les couches adjacentes de manière hiérarchique et bien définie.

La structure de projet a été conçue de manière a faciliter la maintenance toute en respectant le principe d'ouverture a l'extension et fermé a la modification.

## III-Diagramme de classe

![MultiAgentModel](https://github.com/Callme7liwa/MultiAgentModel-ToyCaseSeir/assets/118975773/6b266735-b6a2-45a6-90d4-b1c2f70cd70c)
