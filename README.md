# Xplain - Assistant Intelligent de Débogage Java

[![Java](https://img.shields.io/badge/Java-23-orange.svg)](https://openjdk.java.net/)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.14.2-blue.svg)](https://quarkus.io/)
[![SolidJS](https://img.shields.io/badge/SolidJS-1.8.22-2c4f7c.svg)](https://www.solidjs.com/)
[![License](https://img.shields.io/badge/License-Academic-green.svg)]()

> 🎓 Projet universitaire développé à l'Université Gustave Eiffel (UGE)

## 📖 Description

Xplain est une application web d'aide au débogage de classes Java qui combine la puissance des modèles de langage (LLM) avec un compilateur Java intégré. Elle reproduit l'interface classique de discussion avec des LLM modernes avec un historique de conversations, où chaque conversation consiste en une série d'interactions entre l'utilisateur et Marx, un assistant IA qui peut utiliser différents modèles parmi une liste disponible.

### Fonctionnement

Un message de l'utilisateur doit contenir une classe Java. Une première réponse sera celle du compilateur qui affichera les messages de compilation, qui seront suivis par la correction "intelligente" du LLM courant.

## ✨ Caractéristiques principales

- 🔧 **Compilation Java en temps réel** avec messages d'erreur détaillés
- 🤖 **3 modèles LLM disponibles** : Mistral 7B, Yi-Coder 1.5B, Llama 3.2 3B
- 💬 **Interface chat moderne** construite avec SolidJS
- ⚡ **Streaming en temps réel** des réponses IA via WebSockets
- 💾 **Persistance automatique** des conversations avec HSQLDB
- 📝 **Support Markdown** et coloration syntaxique du code
- 🔒 **Exécution 100% locale** - aucune donnée envoyée à des services externes

## 🚀 Démarrage rapide

### Prérequis

- GraalVM 23+
- Maven 3.9+
- Node.js 22+ et npm 11+
- 8 GB RAM minimum (16 GB recommandé)

### Installation

```bash
# Cloner le repository
git clone https://github.com/MaxTekpa494/Xplain.git
cd Xplain/Xplain

# Construire l'application (télécharge automatiquement les modèles LLM)
./mvnw package

# Lancer l'application
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -jar target/Xplain-runner.jar
```

### Accès

- **Interface web** : http://localhost:8080
- **Swagger UI** : http://localhost:8080/q/swagger-ui
- **OpenAPI** : http://localhost:8080/q/openapi

## 🏗️ Architecture

### Stack technologique

**Backend**
- Quarkus 3.14.2 (Java 23)
- JLama 0.8.2 (modèles LLM locaux)
- HSQLDB 2.7.3
- JDBI 3.45.1
- WebSockets + REST API

**Frontend**
- SolidJS 1.8.22
- Vite 5.4.9
- Bulma CSS 1.0.2
- Prism.js (syntax highlighting)
- Marked (markdown rendering)

### Architecture en couches

```
Frontend (SolidJS) ↔ WebSocket/REST API ↔ Backend (Quarkus)
                                              ↓
                    ┌─────────────────────────┼─────────────────────────┐
                    │                         │                         │
            CompilerService            LLMService               DatabaseService
            (javax.tools)              (JLama)                 (JDBI + HSQLDB)
```

## 📂 Structure du projet

```
Xplain/
├── src/main/
│   ├── java/fr/uge/Xplain/
│   │   ├── compiler/      # Services de compilation Java
│   │   ├── llm/           # Services LLM et WebSocket
│   │   └── dataBase/      # Services de persistance
│   └── webui/             # Application frontend SolidJS
│       ├── app/
│       ├── components/
│       └── services/
└── models/                # Modèles LLM (auto-téléchargés)
```

## 💡 Utilisation

1. **Démarrer une conversation** : Cliquez sur "Nouvelle conversation"
2. **Sélectionner un modèle** : Choisissez parmi Mistral, Yi-Coder ou Llama
3. **Soumettre du code Java** : Collez votre classe Java dans le chat
4. **Recevoir l'analyse** : Obtenez les erreurs du compilateur + les suggestions de l'IA

## 🎓 Compétences démontrées

- **Java moderne** : Java 23, threads virtuels, records, pattern matching
- **Frameworks** : Quarkus, SolidJS, intégration frontend/backend
- **IA & ML** : Intégration de LLM locaux, streaming de réponses
- **Architecture** : Microservices, WebSockets, REST API
- **Base de données** : JDBI, HSQLDB, gestion transactionnelle
- **DevOps** : Maven, build automation, gestion de dépendances lourdes

## 📊 Métriques

- ~1,200 lignes de code Java backend
- ~800 lignes de code JSX frontend
- 3 modèles LLM intégrés (11+ GB total)
- 20+ frameworks et bibliothèques
- Architecture complète full-stack

## 📚 Documentation complète

Pour une description détaillée du projet adaptée à un portfolio, consultez :

- 📄 **[README_PORTFOLIO.md](README_PORTFOLIO.md)** - Documentation complète en français
- 📄 **[README_EN.md](README_EN.md)** - Complete documentation in English
- 📄 **[docs/Documentation technique.pdf](docs/Documentation%20technique.pdf)** - Documentation technique détaillée
- 📄 **[docs/Manuel utilisateur.pdf](docs/Manuel%20utilisateur.pdf)** - Manuel d'utilisation

## 🔮 Évolutions futures

- Support de langages supplémentaires (Python, JavaScript, C++, etc.)
- Intégration de modèles LLM plus récents
- Mode collaboration multi-utilisateurs
- Intégration avec IDE (VS Code, IntelliJ)

## 👨‍💻 Auteur

**MaxTekpa494**

Projet universitaire - Université Gustave Eiffel (UGE)

---

⭐ Si vous trouvez ce projet intéressant, n'hésitez pas à le mettre en favoris !
