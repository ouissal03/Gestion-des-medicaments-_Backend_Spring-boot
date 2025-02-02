**MediCare - Système de gestion de médicaments 💊**
===
MediCare est un système intelligent de gestion de la prise de médicaments conçu pour aider les patients, les personnes âgées et ceux qui ont tendance à oublier leurs médicaments. Il permet :

- De rappeler aux patients de prendre leurs médicaments à temps
- De suivre l'état de prise des médicaments grâce à un pilulier intelligent
- De fournir une interface aux responsables (famille, soignants) pour consulter les états de prise de médicaments des patients
- Le système repose sur une architecture IoT et cloud, intégrant un ESP8266 avec des capteurs, un backend Spring Boot & MongoDB, et un frontend Angular & Bootstrap. La communication entre les composants se fait via RabbitMQ.

**🖥️ Backend (Spring Boot & MongoDB)**
===
Description :
Ce repository contient l'API backend développée avec Spring Boot et MongoDB. Il offre les fonctionnalités suivantes :

- Authentification des utilisateurs (inscription, connexion, modification de profil)
- Gestion des états de prise de médicaments
- Gestion des notifications
- Communication via RabbitMQ pour traiter les messages envoyés par l'ESP8266
- 
Technologies utilisées :

Spring Boot
Spring Security
RabbitMQ
MongoDB
RESTful API
