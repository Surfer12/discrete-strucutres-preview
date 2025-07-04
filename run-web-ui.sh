#!/bin/bash

# Script to run the Agent Web UI
# You can set environment variables here or export them in your shell

echo "Starting Agent Web UI..."
echo "Make sure to set your API keys as environment variables:"
echo "  export MISTRAL_API_KEY='your-key'"
echo "  export OPENAI_API_KEY='your-key'"
echo "  export NOVA_API_KEY='your-key'"
echo ""

# Run the Spring Boot application
./gradlew :web-ui:bootRun