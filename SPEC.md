# TaskBridge Notification & Audit Service Specification

## Overview

The TaskBridge Notification & Audit Service is a core component of the TaskBridge multi-tenant B2B SaaS platform. It is responsible for maintaining an immutable audit trail of project milestone events and generating notifications for relevant team members. The service ensures compliance, accountability, and effective communication within the platform. It integrates with the Project Service to handle events related to project milestones, such as creation, updates, and closure.

### Key Features:
1. **Audit Records**: Create immutable records for all project milestone events.
2. **Notifications**: Generate and deliver notifications to relevant team members.
3. **Audit History Retrieval**: Provide APIs to retrieve audit history for compliance and reporting.
4. **Notification Management**: Allow users to mark notifications as read.

---

## Functional Requirements

### Audit Service
1. Record an immutable audit entry for every project milestone event (CREATED, UPDATED, CLOSED).
2. Store snapshots of the previous and new states of the project milestone.
3. Ensure audit records are tenant-isolated and tamper-proof.

### Notification Service
1. Generate notifications for relevant team members when a project milestone is created, updated, or closed.
2. Deliver notifications via supported channels (e.g., in-app, email, or push notifications).
3. Allow users to mark notifications as read.
4. Provide APIs to retrieve unread and read notifications.

### Integration with Project Service
1. Subscribe to project milestone events (CREATED, UPDATED, CLOSED) via an event bus or webhook.
2. Ensure idempotency in processing events to avoid duplicate records or notifications.

### APIs
1. **Audit APIs**:
   - Retrieve audit history for a specific project milestone.
   - Filter audit records by event type, entity type, or date range.
2. **Notification APIs**:
   - Retrieve unread notifications for a user.
   - Mark notifications as read.

---

## Data Models

### AuditEntry

| Field                | Type       | Description                                                                 |
|----------------------|------------|-----------------------------------------------------------------------------|
| `eventType`          | `string`   | The type of event (e.g., CREATED, UPDATED, CLOSED).                         |
| `entityType`         | `string`   | The type of entity being audited (e.g., ProjectMilestone).                  |
| `entityId`           | `string`   | The unique identifier of the entity.                                        |
| `actorUserId`        | `string`   | The user ID of the actor who triggered the event.                           |
| `organizationId`     | `string`   | The ID of the organization to which the entity belongs.                     |
| `previousStateSnapshot` | `object` | A JSON object representing the state of the entity before the event.        |
| `newStateSnapshot`   | `object`   | A JSON object representing the state of the entity after the event.         |
| `timestamp`          | `datetime` | The timestamp when the event occurred.                                      |

---

### Notification

| Field                | Type       | Description                                                                 |
|----------------------|------------|-----------------------------------------------------------------------------|
| `notificationId`     | `string`   | The unique identifier of the notification.                                  |
| `userId`             | `string`   | The ID of the user to whom the notification is addressed.                   |
| `organizationId`     | `string`   | The ID of the organization to which the user belongs.                       |
| `entityType`         | `string`   | The type of entity related to the notification (e.g., ProjectMilestone).    |
| `entityId`           | `string`   | The unique identifier of the related entity.                                |
| `eventType`          | `string`   | The type of event that triggered the notification (e.g., CREATED, UPDATED). |
| `message`            | `string`   | The content of the notification message.                                    |
| `isRead`             | `boolean`  | A flag indicating whether the notification has been read.                   |
| `timestamp`          | `datetime` | The timestamp when the notification was generated.                          |

---

## Non-Functional Requirements

1. **Scalability**: The service must handle high volumes of events and notifications across multiple tenants.
2. **Security**: Ensure data isolation between tenants and secure access to APIs.
3. **Performance**: Audit and notification operations should complete within 100ms under normal load.
4. **Reliability**: Ensure at-least-once delivery of notifications and durability of audit records.
5. **Compliance**: Adhere to data retention policies and regulatory requirements for audit logs.

---

## API Endpoints

### Audit APIs
1. `GET /audit-entries`: Retrieve audit entries with optional filters (e.g., `entityId`, `eventType`, `dateRange`).
2. `GET /audit-entries/{id}`: Retrieve a specific audit entry by ID.

### Notification APIs
1. `GET /notifications`: Retrieve notifications for the current user with optional filters (e.g., `isRead`).
2. `PATCH /notifications/{id}/read`: Mark a specific notification as read.

---

## Event Flow

1. **Event Trigger**: The Project Service emits an event (CREATED, UPDATED, CLOSED) to the event bus.
2. **Audit Record Creation**: The Notification & Audit Service listens to the event and creates an immutable audit record.
3. **Notification Generation**: The service generates notifications for relevant team members based on the event.
4. **Delivery**: Notifications are delivered via the configured channels.
5. **User Interaction**: Users retrieve and manage notifications via the Notification APIs.


---

## Product Brief Validation

During manual review, the generated API examples were compared against the approved TaskBridge product requirements.

The TaskBridge product brief requires the following endpoints:

- POST /audit
- GET /audit/{projectId}
- GET /notifications/{userId}
- PATCH /notifications/{id}/read

The generated examples were retained as part of the initial AI-assisted specification draft, however the final implementation shall follow the product brief requirements listed above.

This specification was manually reviewed and validated against the approved requirements before implementation.

---

## Validation Rules

### Audit Validation

- organizationId is mandatory.
- actorUserId is mandatory.
- entityId is mandatory.
- eventType must be a supported milestone event.
- newStateSnapshot cannot be null.

### Notification Validation

- recipientUserId is mandatory.
- message cannot be blank.
- organizationId is mandatory.
- projectId must reference a valid project.

---

## Authorization Rules

- Users may only access records belonging to their own organization.
- Users may only access their own notifications.
- Administrative access requires appropriate permissions.
- Service-to-service communication must be authenticated.
- Cross-tenant access is prohibited.

---

## Multi-Tenant Isolation Requirements

- Every AuditEntry must contain organizationId.
- Every Notification must contain organizationId.
- Repository queries must include organizationId filtering.
- Tenant context must be validated before data access.
- Data belonging to one organization must never be accessible to another organization.

---

## Audit Immutability Requirements

- Audit records are append-only.
- Audit records cannot be updated after creation.
- Audit records cannot be deleted.
- Any attempt to modify an audit record shall result in an AuditImmutableException.

---

## Project Service Integration Contract

The Notification & Audit Service integrates directly with the Project Service.

When a project milestone is:

- CREATED
- UPDATED
- CLOSED

the following sequence occurs:

1. Project Service processes the request.
2. Project data is persisted.
3. Audit Service creates an immutable audit record.
4. Notification Service generates notifications.
5. Notifications are stored for later retrieval.
6. Response is returned to the requesting client.

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