# Tailgate Tavern Inventory Management System

A JavaFX desktop application for managing restaurant inventory, suppliers, purchase orders, and price comparisons. Built as a portfolio project demonstrating full-stack Java development with a focus on hospitality industry workflows.

## Features

### Current (Phase 1: Authentication)

- Multi-user authentication with BCrypt password hashing
- Role-based access (Manager / Staff)
- Account lockout protection (5 failed attempts trigger a 10 minute lockout)
- Session management with logout
- JavaFX login UI with form validation and error handling
- MySQL backend with normalized schema (13 tables)

### Planned (Phase 2: Inventory Management)

- Item CRUD with category support
- Multi-supplier price tracking with junction-table relationships
- Low-stock alerts based on configurable par-stock levels
- Stock receiving and usage tracking
- Audit trail for all changes

### Planned (Phase 3: Purchasing)

- Per-supplier purchase order generation
- One-way PO state transitions (Draft to Finalized to Sent)
- Price comparison across suppliers (per-unit normalization)
- CSV price import per supplier

## Tech Stack

- **Language:** Java 21
- **UI Framework:** JavaFX 21
- **Build Tool:** Maven
- **Database:** MySQL 8
- **Database Access:** JDBC with PreparedStatements
- **Password Hashing:** jBCrypt
- **Architecture:** MVC with DAO/Service layer separation

## Project Structure

    src/main/java/com/coreyrobinson/inventory/
      app/         - JavaFX entry point and test classes
      controller/  - JavaFX controllers
      dao/         - Data Access Objects (database operations)
      model/       - Domain entity classes
      service/     - Business logic layer
      session/     - Logged-in user session management
      util/        - Database connection utility

    src/main/resources/
      fxml/        - JavaFX view layouts
      database.properties.example - Template for DB credentials

## Getting Started

### Prerequisites

- JDK 21
- MySQL 8
- Maven 3.6+

### Setup

1. Clone the repository
2. Create a MySQL database named `restaurant_inventory`
3. Run the schema scripts (see `BUSINESS_RULES.md` for schema details)
4. Copy `src/main/resources/database.properties.example` to `database.properties` and fill in your MySQL credentials
5. Run `mvn javafx:run` to start the application

### First Run

Use `RegisterTestUser.java` to create the first Manager account, then log in through the main application.

## Business Rules

Full business rules documentation in `BUSINESS_RULES.md`, covering authentication, inventory, suppliers, purchasing, and reporting.

## Author

Corey Robinson — pursuing a transition from 17 years in hospitality to software development. Currently completing an Associate's degree in Computer Technology at Trident Technical College.