# SkillMates

## Introduction

SkillMates est une plateforme collaborative permettant de mettre en relation des personnes souhaitant développer leurs compétences ensemble. L'application facilite la recherche de partenaires d'apprentissage compatibles en fonction des compétences, des objectifs et des intérêts de chacun.

## Objectifs

- Faciliter la rencontre entre personnes partageant des objectifs d'apprentissage similaires
- Encourager l'apprentissage collaboratif et l'échange de connaissances
- Permettre le suivi des progrès à travers des partenariats structurés
- Offrir un espace de planification et d'organisation des sessions de travail
- Créer une communauté d'entraide basée sur le développement de compétences

## Fonctionnalités

- **Gestion de profil utilisateur** : création de profil avec compétences, objectifs d'apprentissage et centres d'intérêt
- **Système de mise en relation** : recherche et suggestion de partenaires potentiels
- **Gestion des partenariats** : création, acceptation et suivi des partenariats entre utilisateurs
- **Objectifs communs** : définition d'objectifs partagés entre partenaires
- **Planification de sessions** : organisation de rencontres (en ligne ou en personne) avec calendrier
- **Tableau de bord** : visualisation des partenariats actifs et des prochaines sessions

## Technologies utilisées

- **Backend** : Java avec Spring Boot, Spring Cloud
- **Architecture** : Microservices communiquant via API REST
- **Bases de données** :
  - MongoDB pour les données utilisateurs
  - MySQL pour les partenariats et sessions
- **Infrastructure** :
  - Spring Cloud Config pour la configuration centralisée
  - Eureka pour la découverte de services
  - Docker pour la conteneurisation
- **Frontend** : Thymeleaf pour les templates web
- **Sécurité** : Spring Security pour l'authentification et l'autorisation
