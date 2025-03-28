# Kotlin Experiments - Developer Guidelines

## Project Overview
This repository contains Kotlin experiments focusing on various concepts.

## Tech Stack
- **Kotlin**: Version 2.1.20
- **JDK**: Java 21
- **Build System**: Gradle with Kotlin DSL
- **Testing Framework**: JUnit 5

## Project Structure
```
kotlin-experiments/
├── gradle/                  # Gradle wrapper and version catalog
├── <features>/              # Feature modules
│   ├── src/main/kotlin/     # Kotlin source files
│   └── src/test/kotlin/     # Test files (to be added)
|-- <feature/subfeature>/    # Sub-feature modules
│   ├── src/main/kotlin/     # Kotlin source files
│   └── src/test/kotlin/     # Test files (to be added)
├── build.gradle.kts         # Root project build file
├── settings.gradle.kts      # Project settings
└── gradle.properties        # Gradle properties
```

## Building and Running
The project uses the Gradle wrapper for consistent builds:

```bash
# Build the entire project
./gradlew build

# Build a specific module
./gradlew :<feature>:build

# Clean build
./gradlew clean build
```

## Running Tests
Tests are configured to use JUnit 5:

```bash
# Run all tests
./gradlew test

# Run tests for a specific module
./gradlew :<feature>>:test
```

## Best Practices
1. **Code Style**: Follow the official Kotlin style guide (enforced by project settings)
2. **Module Organization**: 
   - Create a new module for each distinct concept
   - Use the standard src/main/kotlin and src/test/kotlin structure
3**Testing**:
   - Write tests for all state transitions
   - Test edge cases and error conditions

## Adding New Modules
To add a new module:
1. Create a new directory at the root level
2. Add the module to settings.gradle.kts: `include(":new-module")`
3. Create a build.gradle.kts file in the module directory
4. Follow the standard project structure for source files