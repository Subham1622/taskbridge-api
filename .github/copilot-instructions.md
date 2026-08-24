# TaskBridge Project - Copilot Instructions

## 1. Technology Stack Standards
- **Backend**: Java 17, Spring Boot 3, Spring Data JPA
- **Database**: PostgreSQL
- **Frontend**: Angular, TypeScript
- **Testing**: JUnit 5, Mockito (backend), Jasmine (frontend)
- **APIs**: RESTful APIs for communication between services

## 2. Package and Layered Architecture Conventions
- Follow a layered architecture:
  - **Controller Layer**: Handles HTTP requests and responses.
  - **Service Layer**: Contains business logic.
  - **Repository Layer**: Handles database interactions.
  - **DTO Layer**: Defines data transfer objects.
- Package structure:
com.taskbridge.<service_name>.<layer>

Example:
com.taskbridge.project.controller
com.taskbridge.project.service
com.taskbridge.project.repository


## 3. Naming Conventions
- Classes: Use `PascalCase` (e.g., `ProjectService`).
- Variables and methods: Use `camelCase` (e.g., `getProjectById`).
- Constants: Use `UPPER_SNAKE_CASE` (e.g., `MAX_RETRIES`).
- DTOs: Suffix with `DTO` (e.g., `ProjectDTO`).
- Repositories: Suffix with `Repository` (e.g., `ProjectRepository`).
- Services: Suffix with `Service` (e.g., `ProjectService`).

## 4. DTO Usage Guidelines
- Use DTOs for all data exchanged between layers and services.
- Avoid exposing entity classes directly in APIs.
- Map entities to DTOs using libraries like MapStruct or manually.

## 5. Repository and Service Layer Responsibilities
- **Repository Layer**:
- Directly interacts with the database.
- Use Spring Data JPA for CRUD operations.
- **Service Layer**:
- Contains business logic.
- Calls repository methods and performs data transformations.

## 6. Logging Standards
- Use `SLF4J` for logging.
- Log at appropriate levels:
- `INFO`: General application flow.
- `DEBUG`: Detailed debugging information.
- `ERROR`: Exceptions and critical failures.
- Avoid logging sensitive data.

## 7. Validation Requirements
- Use `javax.validation` annotations for input validation (e.g., `@NotNull`, `@Size`).
- Validate inputs at the controller level.
- Implement custom validators where necessary.

## 8. Security Requirements
- **Authentication**: Use OAuth2 or JWT for securing APIs.
- **Authorization**: Implement role-based access control (RBAC).
- **Least Privilege**: Ensure services and users have minimal access required.
- **Data Exposure**: Mask sensitive data in logs and responses.

## 9. Multi-Tenant SaaS Organization Isolation Requirements
- Use a tenant identifier (organizationId/tenantId) in all database tables.
- Ensure tenant isolation at the database level using filters or schemas.
- Validate tenant context in all service calls.
- All repository queries MUST filter by organizationId/tenantId.
- Never return or query data without tenant context validation.
- Prevent cross-tenant data access at the service and repository layers.
- Audit and notification records must be scoped to the tenant that owns the project.

## 10. REST API Standards
- Use standard HTTP methods:
- `GET` for retrieval.
- `POST` for creation.
- `PUT` for updates.
- `DELETE` for deletion.
- Use plural nouns for resource names (e.g., `/projects`).
- Return appropriate HTTP status codes.
- Document APIs using OpenAPI/Swagger.

## 11. Database and JPA Standards
- Use PostgreSQL-specific features where applicable (e.g., JSONB).
- Define relationships using JPA annotations (`@OneToMany`, `@ManyToOne`).
- Use `@Transactional` for managing transactions.
- Avoid N+1 queries by using `@EntityGraph` or `JOIN FETCH`.

## 12. Testing Expectations and Minimum Test Coverage
- Write unit tests for all service and repository methods.
- Write integration tests for REST APIs.
- Use Mockito for mocking dependencies.
- Maintain a minimum of **80% test coverage**.

## 13. Documentation Expectations
- Document all public methods and classes using Javadoc.
- Maintain an up-to-date README with setup instructions.
- Use Swagger/OpenAPI for API documentation.

## 14. Git and Pull Request Standards
- Use feature branches for development.
- Follow the commit message format:
<type>(<scope>): <description>

Example: `feat(project): add project creation API`
- Require at least one code review before merging.
- Ensure all tests pass before creating a pull request.

## 15. AI Generated Code Review Requirements

- All AI-generated code must be reviewed before commit.
- Validate business rules manually.
- Validate security requirements manually.
- Validate multi-tenant isolation manually.
- Validate compliance requirements manually.
- No Copilot-generated code may be merged without human review.

## 16. Audit Service Rules

- Audit entries are immutable after creation.
- Update operations on audit records are prohibited.
- Delete operations on audit records are prohibited.

## 17. Notification Service Rules

- Notifications must be organization scoped.
- Notifications cannot expose tenant information.
- Notification generation must occur on all milestone state changes.