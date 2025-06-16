#!/bin/bash

# Exit on any error
set -e

# Clean and package the project
echo "Building the project..."
mvn clean package

# Check if JAR was created
JAR_PATH="target/discrete-strucutres-preview-1.0-SNAPSHOT.jar"
if [ ! -f "$JAR_PATH" ]; then
    echo "Error: JAR file not found at $JAR_PATH"
    exit 1
fi

# Run the CLI with help command
echo "Running CLI with help..."
java -jar "$JAR_PATH" --help

# Provide instructions
echo -e "\nTo run specific commands, use:"
echo "java -jar $JAR_PATH [COMMAND] [OPTIONS]"
echo "Example: java -jar $JAR_PATH truth-table \"A & B\"" 