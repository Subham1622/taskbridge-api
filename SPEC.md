# TaskBridge System Specification

## Overview

TaskBridge is a multi-tenant system designed to manage projects, maintain audit logs, and send notifications. It provides APIs for creating, updating, and retrieving projects, as well as tracking changes and notifying users of relevant events. The system ensures tenant isolation and enforces immutability for audit logs.

---

## Current Architecture

### Project Module

#### Responsibilities
- Create project
- Update project status
- Retrieve projects by team
- Delete project

#### Data Model

### Project

| Field         | Type       | Description                                                         |
|---------------|------------|---------------------------------------------------------------------|
| `id`          | `string`   | The unique identifier of the project.                               |
| `name`        | `string`   | The name of the project.                                            |
| `description` | `string`   | A brief description of the project.                                 |
| `status`      | `string`   | The current status of the project.                                  |
| `teamId`      | `string`   | The identifier of the team associated with the project.             |
| `tenantId`    | `string`   | The identifier of the tenant that owns the project.                 |
| `createdAt`   | `datetime` | The timestamp when the project was created.                         |
| `updatedAt`   | `datetime` | The timestamp when the project was last modified.                   |

#### APIs
- `POST /api/projects` - Create a new project.
- `PATCH /api/projects/{projectId}/status` - Update the status of a project.
- `GET /api/projects/team/{teamId}` - Retrieve all projects for a specific team.
- `DELETE /api/projects/{projectId}` - Delete a project.

---

### Audit Module

#### Responsibilities
- Create immutable audit entries
- Retrieve audit history
- Filter by event type
- Filter by date range

#### Data Model

### AuditEntry

| Field                     | Type       | Description                                                              |
|---------------------------|------------|--------------------------------------------------------------------------|
| `id`                      | `string`   | The unique identifier of the audit entry.                                |
| `eventType`               | `string`   | The type of event (e.g., CREATED, UPDATED, CLOSED).                      |
| `entityType`              | `string`   | The type of entity being audited (e.g., PROJECT).                        |
| `entityId`                | `string`   | The unique identifier of the audited entity.                             |
| `actorUserId`             | `string`   | The identifier of the user or system that triggered the event.           |
| `organizationId`          | `string`   | The identifier of the organization that owns the audited entity.         |
| `previousStateSnapshot`   | `object`   | Snapshot of the entity state before the event occurred.                  |
| `newStateSnapshot`        | `object`   | Snapshot of the entity state after the event occurred.                   |
| `timestamp`               | `datetime` | The timestamp when the audited event occurred.                           |
| `createdAt`               | `datetime` | The timestamp when the audit entry was persisted to the database.        |

#### APIs
- `POST /audit` - Create a new audit entry.
- `GET /audit/{entityId}` - Retrieve audit history for a specific entity.
- `GET /audit/{entityId}/event/{eventType}` - Retrieve audit history filtered by event type.
- `GET /audit/date-range` - Retrieve audit history filtered by a date range.

---

### Notification Module

#### Responsibilities
- Create notifications
- Retrieve notifications
- Retrieve unread notifications
- Mark notifications as read

#### Data Model

### Notification

| Field                | Type       | Description                                       |
|----------------------|------------|---------------------------------------------------|
| `id`                 | `string`   | The unique identifier of the notification.        |
| `recipientUserId`    | `string`   | The identifier of the notification recipient.     |
| `projectId`          | `string`   | The identifier of the related project.            |
| `eventType`          | `string`   | The event that triggered the notification.        |
| `message`            | `string`   | The notification content.                         |
| `readStatus`         | `boolean`  | Indicates whether the notification has been read. |
| `createdTimestamp`   | `datetime` | The timestamp when the notification was created.  |
| `organizationId`     | `string`   | The organization associated with the notification.|

#### APIs
- `GET /notifications/{userId}` - Retrieve all notifications for a user.
- `GET /notifications/{userId}/unread` - Retrieve unread notifications for a user.
- `GET /notifications/project/{projectId}` - Retrieve notifications for a specific project.
- `GET /notifications/event/{eventType}` - Retrieve notifications filtered by event type.
- `PATCH /notifications/{id}/read` - Mark a notification as read.

---

## Validation Rules

### Project Validation

- name is required and cannot be blank.
- description is optional.
- status is required and cannot be blank.
- teamId is required.
- tenantId is supplied through the X-Tenant-ID request header.

### Audit Validation

- eventType is required.
- entityType is required.
- entityId is required.
- actorUserId is required.
- organizationId is required.
- newStateSnapshot is required.
- previousStateSnapshot may be null for CREATED events.

### Notification Validation

- recipientUserId is required.
- projectId is required.
- eventType is required.
- message is required.
- organizationId is required.

---

## Authorization Rules

- **X-Tenant-ID Enforcement**: All requests must include a valid `X-Tenant-ID` header.
- **Tenant Isolation**: Data access is restricted to the tenant associated with the `X-Tenant-ID`.
- **Cross-Tenant Access Prevention**: Requests attempting to access data from other tenants are blocked.

RBAC (Role-Based Access Control) is not implemented but is documented as a future enhancement.

---

## Multi-Tenant Isolation Requirements

The system enforces multi-tenant isolation through:
- Filtering repositories by `tenantId` and `organizationId`.
- Validating tenant and organization IDs in service layers.

Repository methods enforce tenant-aware queries: 
- findByIdAndTenantId(...)
- findByEntityIdAndOrganizationId(...)
- findByIdAndOrganizationId(...)

---

## Audit Immutability Requirements

Audit entries are immutable:
- Updates to existing audit entries are prohibited.
- Deletion of audit entries is prohibited.
- Violations result in an `AuditImmutableException`.

Retention policies are not implemented.

---

## Service Integration Design

The current implementation includes:
- **Project Service**: Manages project lifecycle operations.
- **Audit Service**: Tracks changes and maintains immutable logs.
- **Notification Service**: Sends and retrieves notifications.

Modules are currently implemented as independent components.

Project lifecycle events (CREATED, UPDATED, CLOSED) are intended to drive audit and notification workflows.

Lightweight service integration has been implemented between Project Service, Audit Service, and Notification Service for CREATED, UPDATED, and CLOSED project lifecycle events.

Future enhancements include authenticated actor identity propagation and team-member recipient resolution.

However:
- Full actor identity propagation is not yet implemented.
- Notification recipient resolution is not yet implemented.

## Testing Requirements

The following tests are required to ensure system functionality:
- `shouldNotifyAllTeamMembers()`
- `shouldCreateAuditEntryWhenStatusUpdated()`
- `shouldPreventAuditDeletion()`
- `shouldFilterAuditByDate()`
- `shouldFilterAuditByEventType()`
- `shouldBlockCrossTenantAuditAccess()`

## Known Limitations

- Missing actor identity propagation.
- Notification recipient resolution gap.
- Hard delete compliance considerations.
- Future RBAC enhancements.
- Cross-service integration assumptions.

---

## Non-Functional Requirements

1. **Scalability**: The service must handle high volumes of events and notifications across multiple tenants.
2. **Security**: Ensure data isolation between tenants and secure access to APIs.
3. **Performance**: Audit and notification operations should complete within 100ms under normal load.
4. **Reliability**: Ensure at-least-once delivery of notifications and durability of audit records.
5. **Compliance**: Adhere to data retention policies and regulatory requirements for audit logs.

---

## Product Brief Validation

The initial AI-generated specification was reviewed against the approved TaskBridge requirements.

During review, API contracts, audit requirements, notification requirements, and multi-tenant isolation requirements were validated and updated where necessary.

The final implementation and specification reflect the approved TaskBridge requirements rather than the initial generated draft.

This review provided evidence of human oversight and validation of AI-generated content prior to implementation.

---


### Multi-Tenant Assumption

For the assessment implementation, tenantId and organizationId are treated as the same logical identifier.

The X-Tenant-ID request header is propagated as:

- Project.tenantId
- AuditEntry.organizationId
- Notification.organizationId

A production implementation may introduce separate tenant and organization domain concepts if required.

---

## Copilot Assistance and Human Judgement

### How Copilot Helped

Copilot assisted with:

- Generating the initial specification structure.
- Drafting functional requirements.
- Drafting data models.
- Drafting notification and audit workflows.
- Drafting non-functional requirements.

### Human Judgement Applied

Manual review was required to:

- Validate generated API designs against the TaskBridge product brief.
- Define explicit audit immutability requirements.
- Define authorization requirements.
- Expand multi-tenant isolation requirements.
- Validate service integration expectations.
- Ensure compliance requirements were addressed before implementation.
- DTO and service contract mismatches.
- Repository filtering issues.
- Missing transaction boundaries.
- Audit constructor mismatches.
- Notification entity design concerns.
- Multi-tenant enforcement gaps.
- Cross-service integration assumptions.