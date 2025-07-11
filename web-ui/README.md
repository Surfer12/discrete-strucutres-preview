# Discrete Logic Mathematics Web UI

A modern web interface for discrete logic mathematics concepts and calculations, built with Spring Boot and Thymeleaf, designed for easy migration to React.

## Features

- **Interactive Logic Calculator**: Evaluate complex logical expressions
- **Truth Table Generator**: Generate truth tables for any logical expression
- **Modern UI**: Built with Bootstrap 5 and custom styling
- **React Migration Ready**: Designed for gradual migration to React frontend
- **API Integration**: Ready for integration with Mistral, OpenAI, and Amazon Nova APIs

## Architecture

This module follows a clean architecture pattern:

```
web-ui/
├── src/main/java/com/discretelogic/ui/
│   ├── UiApplication.java              # Main Spring Boot application
│   ├── config/
│   │   └── ApiConfiguration.java       # API keys and configuration
│   ├── controller/
│   │   └── HomeController.java         # Web controllers
│   └── service/
│       └── LogicService.java           # Business logic service
├── src/main/resources/
│   ├── static/
│   │   ├── styles.css                  # Custom CSS
│   │   └── app.js                      # JavaScript with React readiness
│   ├── templates/
│   │   ├── index.html                  # Home page
│   │   ├── calculator.html             # Logic calculator
│   │   ├── truth-tables.html           # Truth table generator
│   │   └── about.html                  # About page
│   └── application.properties          # Spring Boot configuration
└── build.gradle.kts                    # Build configuration
```

## Getting Started

### Prerequisites

- Java 21 or higher
- Gradle 8.0 or higher
- Environment variables for API keys (optional)

### Environment Variables

Set these environment variables for API integration:

```bash
export MISTRAL_API_KEY="your-mistral-api-key"
export OPENAI_API_KEY="your-openai-api-key"
export NOVA_API_KEY="your-nova-api-key"
```

### Running the Application

1. **Build the project**:
   ```bash
   ./gradlew :web-ui:build
   ```

2. **Run the application**:
   ```bash
   ./gradlew :web-ui:bootRun
   ```

3. **Access the application**:
   Open your browser to `http://localhost:8080`

### Running from IDE

You can also run the application directly from your IDE by running the `UiApplication` class.

## Pages and Features

### Home Page (`/`)
- Welcome page with feature overview
- Links to calculator and truth table generator
- Modern hero section with gradient background

### Logic Calculator (`/calculator`)
- Input field for logical expressions
- Real-time expression validation
- Support for operators: AND, OR, NOT, XOR, NAND, NOR, IMPLIES, BICONDITIONAL
- Quick-insert buttons for common operators
- Results display with detailed feedback

### Truth Tables (`/truth-tables`)
- Generate truth tables for any logical expression
- Variable input and expression input
- Common truth table examples
- Interactive table display

### About Page (`/about`)
- Information about discrete logic mathematics
- Key concepts and applications
- Tool features and future enhancements

## Supported Logical Operators

- **AND**: Logical AND (∧)
- **OR**: Logical OR (∨)
- **NOT**: Logical NOT (¬)
- **XOR**: Exclusive OR (⊕)
- **NAND**: NOT AND (↑)
- **NOR**: NOT OR (↓)
- **IMPLIES**: Implication (→)
- **BICONDITIONAL**: If and only if (↔)

## React Migration Strategy

This application is designed for gradual migration to React:

### Phase 1: Current State (Thymeleaf)
- ✅ Full Thymeleaf-based UI
- ✅ Bootstrap styling
- ✅ JavaScript enhancements
- ✅ React root elements ready

### Phase 2: Hybrid Approach
- Add React components for specific sections
- Mount React components in designated `#react-root` divs
- Keep existing Thymeleaf pages as fallbacks

### Phase 3: Full React Migration
- Convert all pages to React components
- Transform controllers to REST API endpoints
- Maintain backward compatibility

## Development

### Project Structure

The project follows Spring Boot conventions:
- `src/main/java/`: Java source code
- `src/main/resources/templates/`: Thymeleaf templates
- `src/main/resources/static/`: Static assets (CSS, JS, images)
- `src/main/resources/application.properties`: Configuration

### Adding New Features

1. **Add new endpoints** in `HomeController`
2. **Create corresponding templates** in `templates/`
3. **Add business logic** in `LogicService`
4. **Update navigation** in all templates

### CSS Customization

Custom styles are defined in `src/main/resources/static/styles.css`:
- CSS variables for consistent theming
- Bootstrap overrides
- React-ready utility classes
- Responsive design utilities

### JavaScript Enhancements

The `app.js` file includes:
- Form validation and enhancement
- Interactive features
- React migration utilities
- API integration helpers

## API Integration

The application is configured to work with external APIs:

### Configuration
API keys are managed through `ApiConfiguration.java` and environment variables.

### Usage Example
```java
@Autowired
private ApiConfiguration apiConfig;

// Use API keys
String mistralKey = apiConfig.getMistralApiKey();
String openAiKey = apiConfig.getOpenAiApiKey();
String novaKey = apiConfig.getAmazonNovaApiKey();
```

## Testing

### Running Tests
```bash
./gradlew :web-ui:test
```

### Test Structure
- Unit tests for services
- Integration tests for controllers
- Web layer tests with `@WebMvcTest`

## Deployment

### Production Build
```bash
./gradlew :web-ui:build
```

### Docker Support
Create a `Dockerfile` in the web-ui directory:
```dockerfile
FROM openjdk:21-jdk-slim
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## Contributing

1. Follow the existing code style
2. Add tests for new features
3. Update documentation
4. Ensure React migration compatibility

## License

This project is part of the Discrete Logic Mathematics library.