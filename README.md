English | [日本語](README_ja.md)

# Android Clean Architecture Demo App

## Overview
This application was created for the purpose of validating and demonstrating the following technologies:
- Clean Architecture
- Jetpack Compose
- Navigation3
- Hilt (Dependency Injection)
- StateFlow / SharedFlow
- Retrofit / OkHttp
- DataStore

## Architecture

The project follows a three-layer architecture:
> UI / Domain / Data

### UI Layer
The UI layer is responsible for rendering screens and handling user interactions.
- theme : Defines the application's design system, including:
  - Colors, 
  - Typography, 
  - Global styling configurations
- widgets : Reusable UI components that are not tied to specific feature screens.
- feature : Contains business-specific screens, including:
  - UI rendering logic (Compose screens)
  - Corresponding ViewModels

### Domain Layer
The core of the architecture. This layer does not depend on any other layer.
- interfaces : Defines abstractions (interfaces) for data access.
- model : Contains pure business domain models.
- usecase : Encapsulates business logic and specific processing flows.

### Data Layer
Responsible for retrieving, storing, and managing all data sources.
- api : Network-related implementation, including:
  - Retrofit configuration 
  - OkHttp setup 
  - API interface definitions
- datastore : Implements local data persistence using DataStore.
- repository : Concrete implementations of the repository interfaces defined in the Domain layer.

Control Flow (Call Order)
```
UI (Page)
  → ViewModel
    → UseCase
      → Repository (Interface)
        → RepositoryImpl
          → DataSource (API / DataStore)
```

Dependency Direction
`feature → domain ← data`

The UI layer depends on Domain
The Data layer depends on Domain
The Domain layer is independent