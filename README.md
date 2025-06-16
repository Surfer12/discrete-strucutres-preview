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

# Discrete Logic CLI

## Overview
A comprehensive CLI tool for discrete mathematics operations, supporting various mathematical computations and explorations.

## Prerequisites
- Java 11 or higher
- Maven

## Building the Project
```bash
mvn clean package
```

## Running the CLI

### General Usage
```bash
java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar [COMMAND] [OPTIONS]
```

### Available Commands

1. **Truth Table Generation**
   ```bash
   java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar truth-table "A & B"
   ```
   Options:
   - `-f, --format`: Output format (table, binary, analysis)
   - `-v, --verbose`: Show detailed analysis

2. **Boolean Algebra**
   ```bash
   java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar boolean-algebra "A | B"
   ```
   Options:
   - `-s, --simplify`: Simplify the expression
   - `-d, --dnf`: Convert to Disjunctive Normal Form
   - `-c, --cnf`: Convert to Conjunctive Normal Form

3. **Logic Gates**
   ```bash
   java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar logic-gates -g AND
   ```
   Options:
   - `-g, --gate`: Specify gate type (AND, OR, NOT, NAND, NOR, XOR, XNOR)
   - `-l, --list`: List all available gates
   - `-d, --demo`: Run demonstration (half-adder, full-adder)

4. **Set Operations**
   ```bash
   java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar sets
   ```
   Options:
   - `-d, --demo`: Run demonstration
   - `-p, --power-set`: Show power set demonstration
   - `-m, --demorgan`: Verify De Morgan's laws

5. **Number Conversion**
   ```bash
   java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar convert 42
   ```
   Options:
   - `-f, --from`: Source base (decimal, binary, octal, hex)
   - `-t, --to`: Target base (decimal, binary, octal, hex, all)
   - `-a, --arithmetic`: Perform binary arithmetic
   - `-s, --steps`: Show conversion steps

6. **Combinatorics**
   ```bash
   java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar combinatorics -n 5 -r 3
   ```
   Options:
   - `-n`: Value of n
   - `-r`: Value of r
   - `-f, --factorial`: Calculate factorial
   - `-p, --permutations`: Calculate permutations
   - `-c, --combinations`: Calculate combinations

7. **Karnaugh Map**
   ```bash
   java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar karnaugh "A & B"
   ```
   Options:
   - `-a, --analyze`: Analyze the K-map for groupings

8. **Tutorial**
   ```bash
   java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar tutorial -t boolean
   ```
   Options:
   - `-t, --topic`: Tutorial topic
   - `-l, --list`: List available tutorials

9. **Quiz**
   ```bash
   java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar quiz -t boolean
   ```
   Options:
   - `-t, --topic`: Quiz topic
   - `-q, --questions`: Number of questions

## Help
For general help or command-specific help:
```bash
java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar --help
java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar [COMMAND] --help
```

## Examples
- Generate a truth table: `java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar truth-table "A & B"`
- Simplify a boolean expression: `java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar boolean-algebra -s "A | (A & B)"`
- Demonstrate logic gates: `java -jar target/discrete-strucutres-preview-1.0-SNAPSHOT.jar logic-gates -d half-adder`

## Contributing
Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## License
This project is licensed under the MIT License - see the LICENSE.md file for details. 