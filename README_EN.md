# Xplain - Intelligent Java Debugging Assistant

## 🎯 Project Overview

Xplain is a modern and innovative web application designed to assist Java developers in debugging and fixing their code. The project combines the power of Language Models (LLMs) with an integrated Java compiler to provide an AI-assisted debugging experience.

### General Description

The application replicates the classic chat interface of modern LLMs (Large Language Models), enriched with persistent conversation history. Each conversation consists of a series of interactions between the user and the AI assistant "Marx", which can use different specialized language models.

**Typical workflow:**
1. The user submits a Java class containing potential errors
2. The integrated Java compiler analyzes the code and generates compilation messages
3. The selected LLM model analyzes the errors and suggests intelligent corrections
4. The user receives both the compiler diagnostics and the AI's improvement suggestions

## 🚀 Main Features

### 1. Real-time Java Compilation
- Integration of native Java compiler (javax.tools)
- Instant syntax analysis and error detection
- Support for Java classes, interfaces, enums, and records
- Detailed and precise error messages

### 2. Multi-model AI Assistance
- **Mistral 7B**: Robust model for in-depth analysis
- **Yi-Coder 1.5B**: Specialized for code and optimized for speed
- **Llama 3.2 3B**: Versatile model with good context understanding

### 3. Modern User Interface
- Intuitive chat interface built with SolidJS
- Automatically saved conversation history
- Management of multiple conversations with customizable titles
- Markdown support for rich formatting
- Syntax highlighting with Prism.js
- Responsive interface with Bulma CSS

### 4. Real-time Streaming
- LLM responses streamed in real-time via WebSockets
- Smooth user experience without page reloads
- Asynchronous request handling with Java virtual threads

### 5. Data Persistence
- Embedded HSQLDB database
- Automatic saving of all conversations
- Message management with metadata (timestamp, sender, errors)
- REST API for data manipulation

## 🏗️ Technical Architecture

### Technology Stack

#### Backend (Java 23)
- **Framework**: Quarkus 3.14.2 (modern and performant Java framework)
- **LLM Integration**: JLama 0.8.2 (local language model execution)
- **Database**: HSQLDB 2.7.3 (embedded database)
- **ORM**: JDBI 3.45.1 (lightweight object-relational mapping)
- **REST API**: JAX-RS with Quarkus REST
- **WebSockets**: For real-time bidirectional communication
- **API Documentation**: OpenAPI/Swagger UI
- **Logging**: Logback + SLF4J

#### Frontend (SolidJS)
- **Framework**: SolidJS 1.8.22 (high-performance reactive framework)
- **Router**: @solidjs/router 0.15.1
- **Build tool**: Vite 5.4.9
- **CSS Framework**: Bulma 1.0.2
- **Markdown**: Marked 15.0.4
- **Syntax highlighting**: Prism.js 1.29.0

#### Development Tools
- **Build**: Maven 3.9+
- **JVM**: GraalVM 23+ (performance optimizations)
- **Integration**: Quinoa (transparent frontend/backend integration)
- **Tests**: JUnit 5, REST Assured

### Application Architecture

```
┌─────────────────────────────────────────────────┐
│           Web Interface (SolidJS)               │
│  ┌──────────────┐  ┌──────────────────────────┐ │
│  │   Chat UI    │  │  Conversation Manager    │ │
│  └──────────────┘  └──────────────────────────┘ │
└────────────┬────────────────────────────────────┘
             │ WebSocket + REST API
┌────────────▼────────────────────────────────────┐
│            Quarkus Backend (Java)               │
│  ┌──────────────┐  ┌──────────────────────────┐ │
│  │   REST API   │  │    WebSocket Handler     │ │
│  └──────┬───────┘  └───────────┬──────────────┘ │
│         │                      │                 │
│  ┌──────▼──────────────────────▼──────────────┐ │
│  │         Service Layer (Business Logic)     │ │
│  │  - CompilerService : Java Compilation      │ │
│  │  - LLMService : AI Model Management        │ │
│  │  - DatabaseService : Data Persistence      │ │
│  └──────┬───────────────────────┬─────────────┘ │
└─────────┼───────────────────────┼───────────────┘
          │                       │
┌─────────▼──────────┐  ┌─────────▼──────────────┐
│  Java Compiler API │  │   JLama (Local LLM)    │
└────────────────────┘  │  - Mistral 7B          │
                        │  - Yi-Coder 1.5B       │
┌────────────────────┐  │  - Llama 3.2 3B        │
│   HSQLDB Database  │  └────────────────────────┘
│  - Conversations   │
│  - Messages        │
└────────────────────┘
```

### Key Components

1. **CompilerService**: Manages Java compilation using javax.tools API
2. **LLMService**: Orchestrates language models and generates responses
3. **DatabaseService**: Manages persistence with JDBI and HSQLDB
4. **LLMStreamWebSocket**: Handles WebSocket connections for streaming
5. **ChatInterface** (Frontend): Main user interface

## 📦 Project Structure

```
Xplain/
├── pom.xml                          # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/fr/uge/Xplain/
│   │   │   ├── Main.java            # Entry point
│   │   │   ├── compiler/            # Compilation services
│   │   │   │   ├── CompilerService.java
│   │   │   │   └── CompilerResource.java
│   │   │   ├── llm/                 # LLM services
│   │   │   │   ├── LLMService.java
│   │   │   │   ├── LLMStreamWebSocket.java
│   │   │   │   └── UserCompilerPayload.java
│   │   │   └── dataBase/            # Database services
│   │   │       ├── DataBaseService.java
│   │   │       ├── conversation/    # Conversation models
│   │   │       └── message/         # Message models
│   │   ├── webui/                   # Frontend application
│   │   │   ├── app/                 # Main component
│   │   │   ├── components/          # Reusable components
│   │   │   │   ├── ChatInterface/
│   │   │   │   └── MessageList/
│   │   │   ├── services/            # API services
│   │   │   └── package.json
│   │   └── resources/               # Quarkus configuration
│   └── test/                        # Unit and integration tests
├── models/                          # LLM models (auto-downloaded)
└── docs/                            # Documentation
    ├── Documentation technique.pdf
    └── Manuel utilisateur.pdf
```

## 🔧 Installation and Setup

### System Requirements

- **GraalVM** 23+ (JDK)
- **Maven** 3.9+
- **Node.js** 22+ and npm 11+
- Operating System: Linux x86_64, MacOS x86_64, or Windows x86_64
- RAM: minimum 8 GB (recommended 16 GB for LLM models)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/MaxTekpa494/Xplain.git
cd Xplain/Xplain
```

2. **Build the application**
```bash
./mvnw package
```
This command will:
- Compile the Java code
- Build the frontend with Vite
- Download LLM models if necessary (several GB)
- Create the executable artifact

3. **Launch the application**
```bash
java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -jar target/Xplain-runner.jar
```

4. **Access the application**
- Web interface: http://localhost:8080
- Swagger UI: http://localhost:8080/q/swagger-ui
- OpenAPI: http://localhost:8080/q/openapi

## 💡 Usage

### Starting a New Conversation

1. Access the main interface
2. Click on "New conversation"
3. Select the desired LLM model from the toolbar
4. Paste your Java code in the text area
5. Press Enter or click "Send"

### Interpreting Results

The application will display three types of responses:
1. **Your code**: The Java code you submitted
2. **Compiler output**: Errors and warnings from the Java compiler
3. **AI analysis**: Error explanation and corrected code suggested by the LLM

### Managing Conversations

- **Rename**: Click on the title to edit it
- **Delete**: Use the delete icon
- **Resume**: Click on a conversation to continue it
- **History**: All conversations are automatically saved

## 🎨 Notable Technical Points

### 1. Performance Optimizations
- **Java virtual threads**: Efficient handling of thousands of concurrent connections
- **Response streaming**: Progressive display of LLM responses
- **Lazy model loading**: Models are only loaded on demand
- **Compilation cache**: Reuse of Java compiler

### 2. User Experience
- **Reactive interface**: SolidJS offers exceptional performance
- **Markdown and code formatting**: Professional display of responses
- **Real-time feedback**: User sees the response being built
- **Robust error handling**: Clear messages in case of problems

### 3. Modular Architecture
- **Separation of concerns**: Distinct services for each functionality
- **Dependency injection**: Quarkus CDI for clean management
- **Documented REST API**: OpenAPI for easy integration
- **Embedded database**: No external configuration required

## 🔒 Security and Privacy

- **Local execution**: LLM models run locally, no data sent to external services
- **Code isolation**: Compiled Java code is not executed, only analyzed
- **Local database**: All conversations remain on the user's machine

## 📊 Project Metrics

- **Java code lines**: ~1,200 lines
- **Frontend components**: ~800 JSX lines
- **Unit tests**: JUnit 5 + REST Assured
- **LLM model sizes**: 
  - Yi-Coder: ~1.5 GB
  - Llama 3.2: ~3 GB
  - Mistral 7B: ~7 GB
- **Frameworks and libraries**: 20+ dependencies

## 🎓 Demonstrated Skills

### Backend Development
- Mastery of modern Java (Java 23, preview features)
- Microservices architecture with Quarkus
- Integration of local AI models
- Asynchronous and reactive programming
- Database management with JDBI

### Frontend Development
- SolidJS framework (fine-grained reactivity)
- Bidirectional WebSocket communication
- Complex state management
- Modern and responsive UI/UX

### DevOps and Tools
- Maven for build automation
- Automatic download of heavy dependencies
- Multi-profile configuration (Linux/Mac/Windows)
- API documentation with OpenAPI

### Artificial Intelligence
- LLM (Large Language Models) integration
- Model-specific prompt management
- AI response streaming
- Optimization for local execution

## 🔮 Future Perspectives

- Support for additional languages (Python, JavaScript, C++, etc.)
- Integration of newer and more performant LLM models
- Advanced static analysis features
- Multi-user collaboration mode
- Conversation export/import
- Code refactoring suggestions
- IDE integration (VS Code, IntelliJ)

## 📝 License and Contribution

University project developed at Gustave Eiffel University (UGE) as part of an academic project.

---

**Developed with ❤️ by MaxTekpa494**

*For more technical information, consult the documentation in the `/docs` folder*
