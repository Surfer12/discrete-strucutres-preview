#!/bin/bash

# Get the directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Path to the JAR file
JAR_PATH="$SCRIPT_DIR/target/discrete-logic-java-1.0.0.jar"

# Check if the JAR file exists
if [ ! -f "$JAR_PATH" ]; then
    echo "Error: JAR file not found at $JAR_PATH"
    echo "Please run 'mvn package' first to build the project."
    exit 1
fi

# Run the JAR with all passed arguments
java -jar "$JAR_PATH" "$@"
