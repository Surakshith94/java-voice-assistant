#!/bin/bash

# Clean previous build
./gradlew clean

# Build the project
./gradlew build

# Create logs directory
mkdir -p logs

# Run the application
java -jar build/libs/java-voice-assistant-1.0-SNAPSHOT.jar