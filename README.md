# Discrete Structures with Cognitive Flow Integration

## Project Overview

This project explores the intersection of discrete mathematics, cognitive science, and fluid dynamics through a comprehensive Java implementation. By combining formal mathematical frameworks with cognitive processing models, we aim to create novel approaches to analyzing and understanding complex systems.

## Project Structure

### Main Packages

- `cognitive/`: Cognitive processing and emergence frameworks
  - `emergence/`: Pattern recognition and flow state analysis
  - `processing/`: Recursive and adaptive processing models
  - `integration/`: Bridging mathematical and cognitive concepts

- `discrete/`: Discrete mathematical structures
  - `sets/`: Advanced set theory implementations
  - `graphs/`: Graph theory with flow state representations
  - `logic/`: Formal logic and state verification

- `fluid/`: Fluid dynamics modeling
  - `dynamics/`: Wave patterns and fluid emergence
  - `analysis/`: Pattern and flow analysis frameworks
  
### Documentation
- `docs/`: Comprehensive documentation
  - `notes/`: Project notes and additional documentation
  - `research/`: Research papers and technical notes
  - `syllabus/`: Course-related documents
  - `dsl-notes/`: Domain-Specific Language design notes

### Examples
- `examples/`: Sample implementations and code snippets
  - `java/`: Java code examples
## Key Features

- Integrated cognitive-mathematical framework
- Fluid dynamics modeling using discrete structures
- Formal verification of cognitive states
- Graph-based analysis tools
- Set theory applications in flow modeling

## Getting Started

1. Ensure you have Java 11+ installed
2. Clone the repository
3. Build the project using Maven or Gradle
4. Explore documentation in the `docs/` directory
5. Check out example implementations in the `examples/` directory

## Research Applications

- Flow state analysis in extreme sports
- Cognitive processing pattern recognition
- Fluid dynamics discrete modeling
- Neural network representations

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push and create a pull request

## Documentation Highlights

- Detailed course syllabus in `docs/syllabus/`
- Research notes on concurrent algorithms and data structures
- Domain-Specific Language (DSL) design documentation

## License

[Specify your license here]

## Contact

[Your contact information]

# Discrete Logic Mathematics Library

This is a comprehensive Java library and CLI tool for discrete logic mathematics concepts. It demonstrates modern Java 24 features including Stream Gatherers API.

## Features

- Boolean algebra operations
- Truth tables
- Logic gates simulation
- Set theory operations
- Number system conversions
- Combinatorial calculations
- Karnaugh maps

## Building the Project

The project can be built using either Maven or Gradle.

### Using Maven

To build with Maven:

```bash
mvn compile
mvn package
```

This will compile the code and create a JAR file in the `target` directory.

### Using Gradle

To build with Gradle:

```bash
./gradlew build
```

This will compile the code and create a JAR file in the `build/libs` directory.

## Running the Application

### Using Maven

To run the application with Maven:

```bash
mvn exec:java -Dexec.mainClass="com.discretelogic.app.DiscreteLogicCLI"
```

To run a specific command:

```bash
mvn exec:java -Dexec.mainClass="com.discretelogic.app.DiscreteLogicCLI" -Dexec.args="truth-table 'a & b'"
```

### Using Gradle

To run the application with Gradle:

```bash
./gradlew run
```

To run a specific command:

```bash
./gradlew run --args="truth-table 'a & b'"
```

## Available Commands

- `truth-table`: Generate truth tables for boolean expressions
- `boolean-algebra`: Perform boolean algebra operations
- `logic-gates`: Simulate logic gates and circuits
- `sets`: Perform set theory operations
- `convert`: Convert between number systems
- `combinatorics`: Perform combinatorial calculations
- `karnaugh`: Generate and analyze Karnaugh maps
- `tutorial`: Interactive tutorials on discrete mathematics
- `quiz`: Take quizzes on discrete mathematics topics

## Examples

### Generate a Truth Table

```bash
mvn exec:java -Dexec.mainClass="com.discretelogic.app.DiscreteLogicCLI" -Dexec.args="truth-table 'a & b'"
```

### Convert a Number Between Systems

```bash
mvn exec:java -Dexec.mainClass="com.discretelogic.app.DiscreteLogicCLI" -Dexec.args="convert 42 -f decimal -t binary"
```

### Perform Set Operations

```bash
mvn exec:java -Dexec.mainClass="com.discretelogic.app.DiscreteLogicCLI" -Dexec.args="sets --demo"
```

## Project Structure

- `src/main/java/com/discretelogic/model`: Core domain model classes
- `src/main/java/com/discretelogic/discrete`: Discrete mathematics implementations
- `src/main/java/com/discretelogic/expressions`: Expression parsing and evaluation
- `src/main/java/com/discretelogic/LogicGateSim`: Logic gate simulator
- `src/main/java/com/discretelogic/app`: CLI application

## License

This project is licensed under the MIT License - see the LICENSE file for details. 