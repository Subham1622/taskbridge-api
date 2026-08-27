# Architecture Overview

## PROJECT STRUCTURE

taskbridge-api/
│
├── .github/
│   └── copilot-instructions.md
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── taskbridge/
│   │   │
│   │   │           ├── projects/
│   │   │           │   ├── controller/
│   │   │           │   │   └── ProjectController.java
│   │   │           │   ├── dto/
│   │   │           │   │   ├── ProjectRequestDTO.java
│   │   │           │   │   └── ProjectResponseDTO.java
│   │   │           │   ├── entity/
│   │   │           │   │   └── Project.java
│   │   │           │   ├── exception/
│   │   │           │   │   ├── ProjectNotFoundException.java
│   │   │           │   │   └── GlobalExceptionHandler.java
│   │   │           │   ├── repository/
│   │   │           │   │   └── ProjectRepository.java
│   │   │           │   └── service/
│   │   │           │       └── ProjectService.java
│   │   │
│   │   │           ├── audit/
│   │   │           │   ├── controller/
│   │   │           │   │   └── AuditController.java
│   │   │           │   ├── dto/
│   │   │           │   │   ├── AuditRequestDTO.java
│   │   │           │   │   └── AuditResponseDTO.java
│   │   │           │   ├── entity/
│   │   │           │   │   └── AuditEntry.java
│   │   │           │   ├── exception/
│   │   │           │   │   └── AuditImmutableException.java
│   │   │           │   ├── repository/
│   │   │           │   │   └── AuditRepository.java
│   │   │           │   └── service/
│   │   │           │       └── AuditService.java
│   │   │
│   │   │           ├── notifications/
│   │   │           │   ├── controller/
│   │   │           │   │   └── NotificationController.java
│   │   │           │   ├── dto/
│   │   │           │   │   └── NotificationResponseDTO.java
│   │   │           │   ├── entity/
│   │   │           │   │   └── Notification.java
│   │   │           │   ├── exception/
│   │   │           │   │   └── NotificationNotFoundException.java
│   │   │           │   ├── repository/
│   │   │           │   │   └── NotificationRepository.java
│   │   │           │   └── service/
│   │   │           │       └── NotificationService.java
│   │   │
│   │   │           └── TaskBridgeApplication.java
│   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── taskbridge/
│
│                   ├── projects/
│                   │    └── ProjectServiceTest.java
│                   │
│                   ├── audit/
│                   │    └── AuditServiceTest.java
│                   │
│                   └── notifications/
│                       └── NotificationServiceTest.java
│
├── docs/
│   └── screenshots/
│       ├── README.md
│       ├── 01-h2-tables.png
│       ├── 02-create-project-postman.png
│       ├── 03-projects-table.png
│       ├── 04-audit-created.png
│       ├── 05-notification-created.png
│       ├── 06-update-project-postman.png
│       ├── 07-audit-status-transition.png
│       ├── 08-notification-update-event.png
│       ├── 09-audit-lifecycle-history.png
│       ├── 10-notification-lifecycle-history.png
│       ├── 11-delete-project-postman.png
│       ├── 12-project-deleted-audit-retained.png
│       ├── 13-read-notification-postman.png
│       ├── 14-notification-read-status.png
│       ├── 15-get-notifications-by-user-postman.png
│       ├── 16-get-unread-notifications.png
│       ├── 17-get-audit-history.png
│       ├── 18-audit-date-range-filter.png
│       └── 19-audit-event-type-filter.png
│
├── README.md
├── SPEC.md
├── notes.md
├── ARCHITECTURE.md
├── IMPACT_ANALYSIS.md
├── PROMPTS.md
├── TOOL_STRATEGY.md
├── PR_DESCRIPTION.md
├── REVIEW.md
│
├── pom.xml
│
├── .gitignore
│
└── target


## Modules

### Project Module

Responsibilities:
- Create Project
- Update Project Status
- Retrieve Projects
- Delete Project

### Audit Module

Responsibilities:
- Immutable Audit History
- Event Tracking
- Audit Filtering

### Notification Module

Responsibilities:
- Notification Generation
- Notification Retrieval
- Read Tracking


ProjectService
        |
        +------> AuditService
        |
        +------> NotificationService


## Multi-Tenant Strategy

X-Tenant-ID
        |
        +--> Project.tenantId
        +--> Audit.organizationId
        +--> Notification.organizationId


## Design Decisions

- Layered Architecture
- Service-Oriented Design
- Repository Pattern
- DTO-Based API Contracts
- Immutable Audit Records