# Agent UI - Spring Boot + Thymeleaf Web Interface

A modern web UI for the discrete logic mathematics agent system, built with Spring Boot and Thymeleaf, designed for easy migration to React.

## Features

- ✅ Spring Boot backend with Thymeleaf templating
- ✅ Beautiful, responsive UI with modern design
- ✅ Support for multiple AI providers (Mistral, OpenAI, Amazon Nova)
- ✅ Environment-based API key configuration
- ✅ Designed for seamless React migration
- ✅ Hot-reload development with Spring DevTools

## Quick Start

### Prerequisites

- Java 21 or higher
- Gradle 8.x
- API keys for at least one provider (Mistral, OpenAI, or Amazon Nova)

### Running the Application

1. Set your API keys as environment variables:
   ```bash
   export MISTRAL_API_KEY="your-mistral-key"
   export OPENAI_API_KEY="your-openai-key"
   export NOVA_API_KEY="your-nova-key"
   ```

2. Run the application from the project root:
   ```bash
   ./gradlew :web-ui:bootRun
   ```

3. Open your browser to http://localhost:8080

### Development Mode

The application includes Spring DevTools for hot-reloading during development. Any changes to:
- Java classes
- Thymeleaf templates
- Static resources

Will automatically trigger a restart or live-reload.

## Project Structure

```
web-ui/
├── src/main/java/com/example/ui/
│   ├── UiApplication.java           # Spring Boot main class
│   ├── controller/
│   │   └── HomeController.java      # MVC controllers
│   └── service/
│       └── AgentService.java        # AI provider integration
├── src/main/resources/
│   ├── templates/
│   │   └── index.html              # Thymeleaf templates
│   ├── static/
│   │   ├── styles.css              # CSS styles
│   │   └── app.js                  # JavaScript (React placeholder)
│   └── application.properties       # Spring configuration
└── build.gradle.kts                # Gradle build file
```

## React Migration Strategy

The application is designed for gradual migration to React:

### Phase 0: Current State (Thymeleaf Only)
- Full server-side rendering with Thymeleaf
- No build pipeline required
- API endpoints return both HTML views and JSON

### Phase 1: Introduce React Components
1. Add a `/frontend` directory with Vite or Create React App
2. Build React bundle to `static/app.js`
3. Mount React components in `#react-root` div
4. Example structure:
   ```
   frontend/
   ├── src/
   │   ├── components/
   │   │   └── ChatInterface.jsx
   │   └── main.jsx
   ├── package.json
   └── vite.config.js
   ```

### Phase 2: Incremental Migration
- New features built in React
- Existing Thymeleaf views gradually replaced
- Controllers evolve to REST endpoints
- Same DTOs used for both Thymeleaf models and JSON responses

### Phase 3: Full SPA
- React handles all routing
- Spring Boot serves only JSON APIs
- Thymeleaf templates become error/fallback pages

## API Endpoints

### GET /
- Returns the main Thymeleaf view
- Model attributes:
  - `greeting`: Welcome message
  - `hasApiKeys`: Boolean indicating API configuration
  - `availableProviders`: Map of provider availability
  - `features`: Array of feature descriptions

### POST /api/agent/query
- Queries the AI agent
- Parameters:
  - `prompt`: The user's question
  - `provider`: AI provider to use (mistral, openai, nova)
- Returns JSON response with agent's answer

## Configuration

### application.properties

```properties
# Server settings
server.port=8080

# API Keys (from environment)
# Set MISTRAL_API_KEY, OPENAI_API_KEY, NOVA_API_KEY

# Agent configuration
agent.service.timeout=30000
agent.service.max-retries=3
```

## Adding AI Provider Integrations

To implement actual AI provider calls, update the methods in `AgentService.java`:

```java
private Map<String, Object> queryOpenAI(String prompt) {
    // Replace placeholder with actual OpenAI API call
    // Example: Use OpenAI Java SDK or HTTP client
}
```

## Testing

Run tests with:
```bash
./gradlew :web-ui:test
```

## Building for Production

```bash
./gradlew :web-ui:build
java -jar web-ui/build/libs/web-ui-1.0.0.jar
```

## Troubleshooting

### No API Keys Detected
- Ensure environment variables are set before starting the application
- Check the status bar in the UI header

### Port Already in Use
- Change the port in `application.properties`:
  ```properties
  server.port=8081
  ```

### Hot Reload Not Working
- Ensure Spring DevTools is in the classpath
- IDE may need to be configured for automatic compilation

## Next Steps

1. Implement actual AI provider integrations in `AgentService`
2. Add discrete logic mathematics tools integration
3. Set up frontend build pipeline for React migration
4. Add user authentication and session management
5. Implement WebSocket support for real-time chat