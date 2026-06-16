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
⬇
[ TaskRepository ] ---> Business models mapping
⬇
[ TodoViewModel ] ---> Combines DB Flow + Search State Flow
⬇ Recomputed State: StateFlow
[ Jetpack Compose UI ] ---> Safe collection via collectAsStateWithLifecycle()
