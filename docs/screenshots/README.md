# Screenshot Evidence

## 01-h2-tables.png
Database schema successfully created through JPA entity initialization.

Evidence:
- PROJECTS table created
- AUDIT_ENTRIES table created
- NOTIFICATIONS table created

---

## 02-create-project-postman.png
Project creation API validation.

Evidence:
- POST /api/projects executed successfully.
- Tenant context supplied using X-Tenant-ID.
- Project created successfully.

---

## 03-projects-table.png
Project persistence verification.

Evidence:
- Project record successfully stored.
- Tenant identifier persisted.
- Team association persisted.

---

## 04-audit-created.png
Audit entry creation verification.

Evidence:
- AuditService integration executed.
- CREATED lifecycle event recorded.
- SYSTEM actor recorded.

---

## 05-notification-created.png
Notification generation verification.

Evidence:
- NotificationService integration executed.
- CREATED notification generated.
- Notification recipient stored.

---

## 06-update-project-postman.png
Project status update validation.

Evidence:
- PATCH /api/projects/{id}/status executed successfully.
- Project status changed from CREATED to CLOSED.

---

## 07-audit-status-transition.png
Audit state-transition verification.

Evidence:
- UPDATED audit event created.
- Previous state captured.
- New state captured.
- Audit history preserved.

---

## 08-notification-update-event.png
Notification generation after status update.

Evidence:
- UPDATED notification generated.
- Team recipient resolution applied.
- Notification history retained.

---

## 09-audit-lifecycle-history.png
Project lifecycle audit history verification.

Evidence:
- CREATED event recorded.
- UPDATED event recorded.
- CLOSED event recorded.
- Immutable audit trail maintained.

---

## 10-notification-lifecycle-history.png
Project lifecycle notification history verification.

Evidence:
- CREATED notification generated.
- UPDATED notification generated.
- CLOSED notification generated.
- Notification history retained.

---

## 11-delete-project-postman.png
Project deletion workflow validation.

Evidence:
- DELETE endpoint executed successfully.
- Project removal completed.
- Downstream services invoked before deletion.

---

## 12-project-deleted-audit-retained.png
Compliance and retention verification.

Evidence:
- Project record removed.
- Audit history retained.
- Notification history retained.
- Historical traceability preserved after deletion.

---

## 13-read-notification-postman.png

Notification read-status update validation.

Evidence:

- PATCH /notifications/{id}/read executed successfully.
- API returned HTTP 204 No Content.
- Notification read workflow completed successfully.

---

## 14-notification-read-status.png

Notification read-status persistence verification.

Evidence:

- Notification 1 marked as read.
- READ_STATUS updated to TRUE.
- Notification history preserved.
- Other notifications remain unread.

---

## 15-get-notifications-by-user-postman.png

Notification retrieval API validation.

Evidence:

- GET /notifications/{userId} executed successfully.
- Tenant-scoped notification retrieval verified.
- CREATED notification returned.
- UPDATED notification returned.
- CLOSED notification returned.
- Notification history accessible to recipient TEAM_101.

---

## 16-get-unread-notifications.png

Unread notification retrieval validation.

Evidence:

- GET /notifications/{userId}/unread executed successfully.
- Tenant-scoped retrieval verified.
- Read notifications excluded from results.
- Only unread notifications returned.
- UPDATED notification returned.
- CLOSED notification returned.

---

## 17-get-audit-history.png

Audit history retrieval validation.

Evidence:

- GET /audit/{entityId} executed successfully.
- Tenant-scoped retrieval verified.
- CREATED audit event returned.
- UPDATED audit event returned.
- CLOSED audit event returned.
- Immutable audit history available through API.

---

## 18-audit-date-range-filter.png

Audit date-range filtering validation.

Evidence:

- GET /audit/date-range executed successfully.
- Date-range filtering applied.
- Tenant-scoped retrieval verified.
- Audit history returned within the requested time window.

---

## 19-audit-event-type-filter.png

Audit event-type filtering validation.

Evidence:

- GET /audit/{entityId}/event/{eventType} executed successfully.
- Event-type filtering applied.
- Tenant-scoped retrieval verified.
- Only matching audit events returned.
- UPDATED audit event retrieved successfully.
- Audit query functionality validated.

Assessment Relevance:

- Demonstrates support for event-based audit history retrieval.
- Validates one of the documented testing requirements:
  - shouldFilterAuditByEventType()
- Confirms repository-level filtering and service-layer mapping are functioning correctly.