# Todo App (Android)

A reactive task management application (TODO Board) for Android, built from the ground up using **Modern Android Development (MAD)** practices and **Clean Architecture** principles. The project implements an **Offline-First** architecture pattern, ensuring full core functionality and data persistence without an internet connection.

---

## 🛠 Tech Stack & Architecture

The application features a strict separation of concerns into distinct layers (**Data**, **Domain**, **Presentation/UI**), ensuring high testability, maintainability, and clear scalability.

### Core Technologies (MAD)
* **UI Layer:** Jetpack Compose (Declarative UI, Material 3 components).
* **Architecture:** MVVM (Model-View-ViewModel) + Unidirectional Data Flow (UDF) powered by `StateFlow`.
* **Asynchronous & Reactive:** Kotlin Coroutines & Kotlin Flow (leveraging asynchronous stream operators like `combine` and `stateIn`).
* **Local Storage (Offline-First):** Room DB with reactive data streaming directly via Kotlin `Flow`.
* **Dependency Injection:** Hilt (Dagger 2 abstraction) utilizing custom Android scoping.
* **Build System & Configuration:** Gradle Kotlin DSL (`.gradle.kts`) + centralized Version Catalogs (`libs.versions.toml`) + Kotlin Symbol Processing (KSP).

---

## 📐 Reactive Data Flow

The project strictly adheres to the Unidirectional Data Flow (UDF) pattern:

[ Room Database ] ---> Updates streamed via Flow
[ TaskRepository ] ---> Business models mapping
[ TodoViewModel ] ---> Combines DB Flow + Search State Flow
[ Jetpack Compose UI ] ---> Safe collection via collectAsStateWithLifecycle()

When a user triggers an action (e.g., adding a task or typing a search query), the event is dispatched to the `ViewModel`. The `ViewModel` mutates the internal stream, the `Room` database updates, and the changes automatically ripple back up to update the UI reactively.

---

## 📂 Project Directory Structure

The project uses a clean, layered package structure inside a single module, separating infrastructure from core business rules:

│
├── data/                       # DATA LAYER (DB, Entities, Room Implementation)
│   ├── local/
│   │   ├── db/                 # Room Database configuration
│   │   ├── dao/                # Data Access Objects (SQL queries)
│   │   └── entity/             # DB schema representations (TaskEntity)
│   └── repository/             # Repository implementation & domain mapping
│
├── domain/                     # DOMAIN LAYER (Pure Kotlin Business Logic)
│   ├── model/                  # Pure data models (Task, TaskStatus)
│   └── repository/             # Abstract Repository interfaces
│
├── di/                         # DEPENDENCY INJECTION LAYER
│   └── DatabaseModule.kt       # Hilt module providing DB and Repository singletons
│
└── ui/                         # PRESENTATION LAYER (State & Composed Screens)
├── screens/
│   └── todo/                 # Kanban feature screen
│       ├── TodoScreen.kt
│       ├── TodoViewModel.kt
│       └── TodoUiState.kt
└── theme/                      # Material 3 Design token configurations

## 🧪 Testing Strategy

Core business logic, reactive flow combinations, and UI state mutations within the `TodoViewModel` are fully covered by local **Unit Tests**.

* **MockK:** Used to isolate the presentation layer by stubbing and verifying repository behaviors.
* **Turbine:** Utilized for crisp, synchronous testing of asynchronous Kotlin `StateFlow` emissions.
* **MainDispatcherRule:** A custom JUnit4 rule implemented to seamlessly swap the Android `Dispatchers.Main` thread with an `UnconfinedTestDispatcher` in a JVM test environment.
