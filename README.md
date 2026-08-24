# TaskBridge API

## Technology Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- H2 Database
- JUnit 5
- Mockito

## Services

### Project Service
- Create Project
- Update Project Status
- Get Projects By Team
- Delete Project

### Notification & Audit Service
- Audit Logging
- Notification Generation
- Audit History Retrieval
- Mark Notifications As Read

## Architecture

Controller → Service → Repository → Database

## Running the Application

mvn spring-boot:run

## H2 Console

http://localhost:8080/h2-console