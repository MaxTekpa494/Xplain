# Xplain - Assistant Intelligent de Débogage Java

## 🎯 Vue d'ensemble du projet

Xplain est une application web moderne et innovante conçue pour assister les développeurs Java dans le débogage et la correction de leur code. Le projet combine la puissance des modèles de langage (LLM) avec un compilateur Java intégré pour offrir une expérience de débogage assistée par intelligence artificielle.

### Description générale

L'application reproduit l'interface classique de discussion avec des LLM (Large Language Model) modernes, enrichie d'un historique de conversations persistant. Chaque conversation consiste en une série d'interactions entre l'utilisateur et l'assistant IA "Marx", qui peut utiliser différents modèles de langage spécialisés.

**Flux de travail typique :**
1. L'utilisateur soumet une classe Java contenant des erreurs potentielles
2. Le compilateur Java intégré analyse le code et génère des messages de compilation
3. Le modèle LLM sélectionné analyse les erreurs et propose une correction intelligente
4. L'utilisateur reçoit à la fois le diagnostic du compilateur et la suggestion d'amélioration de l'IA

## 🚀 Fonctionnalités principales

### 1. Compilation Java en temps réel
- Intégration du compilateur Java natif (javax.tools)
- Analyse syntaxique et détection d'erreurs instantanée
- Support des classes, interfaces, enums et records Java
- Messages d'erreur détaillés et précis

### 2. Assistance IA multi-modèles
- **Mistral 7B** : Modèle robuste pour analyse approfondie
- **Yi-Coder 1.5B** : Spécialisé pour le code et optimisé pour la rapidité
- **Llama 3.2 3B** : Modèle polyvalent avec bonne compréhension du contexte

### 3. Interface utilisateur moderne
- Interface de chat intuitive construite avec SolidJS
- Historique de conversations sauvegardé automatiquement
- Gestion de conversations multiples avec titres personnalisables
- Support du markdown pour un formatage riche
- Coloration syntaxique du code avec Prism.js
- Interface responsive avec Bulma CSS

### 4. Streaming en temps réel
- Réponses du LLM diffusées en temps réel via WebSockets
- Expérience utilisateur fluide sans rechargement de page
- Gestion asynchrone des requêtes avec threads virtuels Java

### 5. Persistance des données
- Base de données HSQLDB embarquée
- Sauvegarde automatique de toutes les conversations
- Gestion des messages avec métadonnées (timestamp, sender, erreurs)
- API REST pour manipulation des données

## 🏗️ Architecture technique

### Stack technologique

#### Backend (Java 23)
- **Framework** : Quarkus 3.14.2 (framework Java moderne et performant)
- **LLM Integration** : JLama 0.8.2 (exécution locale de modèles de langage)
- **Base de données** : HSQLDB 2.7.3 (base embarquée)
- **ORM** : JDBI 3.45.1 (mapping objet-relationnel léger)
- **API REST** : JAX-RS avec Quarkus REST
- **WebSockets** : Pour communication bidirectionnelle en temps réel
- **Documentation API** : OpenAPI/Swagger UI
- **Logging** : Logback + SLF4J

#### Frontend (SolidJS)
- **Framework** : SolidJS 1.8.22 (framework réactif performant)
- **Router** : @solidjs/router 0.15.1
- **Build tool** : Vite 5.4.9
- **CSS Framework** : Bulma 1.0.2
- **Markdown** : Marked 15.0.4
- **Syntax highlighting** : Prism.js 1.29.0

#### Outils de développement
- **Build** : Maven 3.9+
- **JVM** : GraalVM 23+ (optimisations performance)
- **Integration** : Quinoa (intégration frontend/backend transparente)
- **Tests** : JUnit 5, REST Assured

### Architecture applicative

```
┌─────────────────────────────────────────────────┐
│           Interface Web (SolidJS)               │
│  ┌──────────────┐  ┌──────────────────────────┐ │
│  │   Chat UI    │  │  Conversation Manager    │ │
│  └──────────────┘  └──────────────────────────┘ │
└────────────┬────────────────────────────────────┘
             │ WebSocket + REST API
┌────────────▼────────────────────────────────────┐
│            Backend Quarkus (Java)               │
│  ┌──────────────┐  ┌──────────────────────────┐ │
│  │   REST API   │  │    WebSocket Handler     │ │
│  └──────┬───────┘  └───────────┬──────────────┘ │
│         │                      │                 │
│  ┌──────▼──────────────────────▼──────────────┐ │
│  │         Service Layer (Business Logic)     │ │
│  │  - CompilerService : Compilation Java      │ │
│  │  - LLMService : Gestion modèles IA         │ │
│  │  - DatabaseService : Persistance données   │ │
│  └──────┬───────────────────────┬─────────────┘ │
└─────────┼───────────────────────┼───────────────┘
          │                       │
┌─────────▼──────────┐  ┌─────────▼──────────────┐
│  Java Compiler API │  │   JLama (LLM local)    │
└────────────────────┘  │  - Mistral 7B          │
                        │  - Yi-Coder 1.5B       │
┌────────────────────┐  │  - Llama 3.2 3B        │
│   HSQLDB Database  │  └────────────────────────┘
│  - Conversations   │
│  - Messages        │
└────────────────────┘
```

### Composants clés

1. **CompilerService** : Gère la compilation Java en utilisant l'API javax.tools
2. **LLMService** : Orchestre les modèles de langage et génère les réponses
3. **DatabaseService** : Gère la persistance avec JDBI et HSQLDB
4. **LLMStreamWebSocket** : Gère les connexions WebSocket pour le streaming
5. **ChatInterface** (Frontend) : Interface utilisateur principale

## 📦 Structure du projet

```
Xplain/
├── pom.xml                          # Configuration Maven
├── src/
│   ├── main/
│   │   ├── java/fr/uge/Xplain/
│   │   │   ├── Main.java            # Point d'entrée
│   │   │   ├── compiler/            # Services de compilation
│   │   │   │   ├── CompilerService.java
│   │   │   │   └── CompilerResource.java
│   │   │   ├── llm/                 # Services LLM
│   │   │   │   ├── LLMService.java
│   │   │   │   ├── LLMStreamWebSocket.java
│   │   │   │   └── UserCompilerPayload.java
│   │   │   └── dataBase/            # Services de base de données
│   │   │       ├── DataBaseService.java
│   │   │       ├── conversation/    # Modèles Conversation
│   │   │       └── message/         # Modèles Message
│   │   ├── webui/                   # Application frontend
│   │   │   ├── app/                 # Composant principal
│   │   │   ├── components/          # Composants réutilisables
│   │   │   │   ├── ChatInterface/
│   │   │   │   └── MessageList/
│   │   │   ├── services/            # Services API
│   │   │   └── package.json
│   │   └── resources/               # Configuration Quarkus
│   └── test/                        # Tests unitaires et d'intégration
├── models/                          # Modèles LLM (téléchargés automatiquement)
└── docs/                            # Documentation
    ├── Documentation technique.pdf
    └── Manuel utilisateur.pdf
```

## 🔧 Installation et démarrage

### Prérequis système

- **GraalVM** 23+ (JDK)
- **Maven** 3.9+
- **Node.js** 22+ et npm 11+
- Système d'exploitation : Linux x86_64, MacOS x86_64 ou Windows x86_64
- Mémoire RAM : minimum 8 GB (recommandé 16 GB pour les modèles LLM)

### Installation

1. **Cloner le repository**
```bash
git clone https://github.com/MaxTekpa494/Xplain.git
cd Xplain/Xplain
```

2. **Construire l'application**
```bash
./mvnw package
```
Cette commande va :
- Compiler le code Java
- Construire le frontend avec Vite
- Télécharger les modèles LLM si nécessaire (plusieurs GB)
- Créer l'artifact exécutable

3. **Lancer l'application**
```bash
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -jar target/Xplain-runner.jar
```

4. **Accéder à l'application**
- Interface web : http://localhost:8080
- Swagger UI : http://localhost:8080/q/swagger-ui
- API OpenAPI : http://localhost:8080/q/openapi

## 💡 Utilisation

### Démarrer une nouvelle conversation

1. Accédez à l'interface principale
2. Cliquez sur "Nouvelle conversation"
3. Sélectionnez le modèle LLM souhaité dans la barre d'outils
4. Collez votre code Java dans la zone de texte
5. Appuyez sur Entrée ou cliquez sur "Envoyer"

### Interpréter les résultats

L'application affichera trois types de réponses :
1. **Votre code** : Le code Java que vous avez soumis
2. **Sortie du compilateur** : Erreurs et avertissements du compilateur Java
3. **Analyse IA** : Explication des erreurs et code corrigé suggéré par le LLM

### Gestion des conversations

- **Renommer** : Cliquez sur le titre pour le modifier
- **Supprimer** : Utilisez l'icône de suppression
- **Reprendre** : Cliquez sur une conversation pour la continuer
- **Historique** : Toutes les conversations sont automatiquement sauvegardées

## 🎨 Points techniques remarquables

### 1. Optimisations de performance
- **Threads virtuels Java** : Gestion efficace de milliers de connexions concurrentes
- **Streaming de réponses** : Affichage progressif des réponses LLM
- **Chargement lazy des modèles** : Les modèles ne sont chargés qu'à la demande
- **Cache de compilation** : Réutilisation du compilateur Java

### 2. Expérience utilisateur
- **Interface réactive** : SolidJS offre des performances exceptionnelles
- **Markdown et code formatting** : Affichage professionnel des réponses
- **Feedback en temps réel** : L'utilisateur voit la réponse se construire
- **Gestion d'erreurs robuste** : Messages clairs en cas de problème

### 3. Architecture modulaire
- **Séparation des responsabilités** : Services distincts pour chaque fonctionnalité
- **Injection de dépendances** : CDI de Quarkus pour gestion propre
- **API REST documentée** : OpenAPI pour intégration facile
- **Base de données embarquée** : Aucune configuration externe nécessaire

## 🔒 Sécurité et confidentialité

- **Exécution locale** : Les modèles LLM tournent localement, aucune donnée envoyée à des services externes
- **Isolation du code** : Le code Java compilé n'est pas exécuté, seulement analysé
- **Base de données locale** : Toutes les conversations restent sur la machine de l'utilisateur

## 📊 Métriques du projet

- **Lignes de code Java** : ~1,200 lignes
- **Composants frontend** : ~800 lignes JSX
- **Tests unitaires** : JUnit 5 + REST Assured
- **Taille des modèles LLM** : 
  - Yi-Coder : ~1.5 GB
  - Llama 3.2 : ~3 GB
  - Mistral 7B : ~7 GB
- **Frameworks et bibliothèques** : 20+ dépendances

## 🎓 Compétences démontrées

### Développement Backend
- Maîtrise de Java moderne (Java 23, features preview)
- Architecture microservices avec Quarkus
- Intégration de modèles IA locaux
- Programmation asynchrone et réactive
- Gestion de bases de données avec JDBI

### Développement Frontend
- Framework SolidJS (réactivité fine-grained)
- Communication WebSocket bidirectionnelle
- Gestion d'état complexe
- UI/UX moderne et responsive

### DevOps et outils
- Maven pour build automation
- Téléchargement automatique de dépendances lourdes
- Configuration multi-profils (Linux/Mac/Windows)
- Documentation API avec OpenAPI

### Intelligence Artificielle
- Intégration de LLM (Large Language Models)
- Gestion de prompts spécialisés par modèle
- Streaming de réponses IA
- Optimisation pour exécution locale

## 🔮 Perspectives d'évolution

- Support de langages supplémentaires (Python, JavaScript, C++, etc.)
- Intégration de modèles LLM plus récents et performants
- Fonctionnalités d'analyse statique avancée
- Mode collaboration multi-utilisateurs
- Export/import de conversations
- Suggestions de refactoring de code
- Intégration avec IDE (VS Code, IntelliJ)

## 📝 Licence et contribution

Projet universitaire développé à l'Université Gustave Eiffel (UGE) dans le cadre d'un projet académique.

---

**Développé avec ❤️ par MaxTekpa494**

*Pour plus d'informations techniques, consultez la documentation dans le dossier `/docs`*
