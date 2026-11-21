# SmartFix AI — Phase 5 Starter

This is a **shared starter**, not a finished implementation of the three use cases. It contains the agreed MySQL schema, Spring Boot project skeleton and a working health endpoint. No reporting, booking or tracking endpoints are implemented yet.

## Requirements
- JDK 17+
- Maven 3.9+
- MySQL 8.0+
- VS Code with Java Extension Pack and Spring Boot Extension Pack

## Setup
1. Import `database/schema.sql` in MySQL Workbench (or run `mysql -u root -p < database/schema.sql`).
2. Set `DB_USER` and `DB_PASSWORD` environment variables for your local MySQL account. Never commit passwords.
3. Run `mvn spring-boot:run` from the project root.
4. Visit `http://localhost:8080/api/health` — expected JSON: `{"status":"UP","project":"SmartFix AI"}`.

**Important:** The starter has no JPA entity classes yet. `ddl-auto=validate` checks mappings once the team adds them. Schema is managed by SQL, not automatic Hibernate table creation.

## Ownership / modules
- **UC-03 Member 3**: `report/` — request creation, media upload, assessment.
- **UC-02 Member 2**: `booking/` — technician recommendations, slots, appointment acceptance.
- **UC-01 Member 1**: `tracking/` — assigned requests, status updates, notifications, completed history.
- **Shared**: `user/`, `common/`, `database/`, `frontend/`.

Each module can contain `controller`, `service`, `repository`, `entity`, `dto`. **Do not create duplicate Java entities for the same database table**: agree on a single owner or move shared entities to `common/entity`.

## API contract (planned, not yet implemented)
| Method | Path | Owner |
|---|---|---|
| POST | `/api/requests` | UC-03 |
| POST | `/api/requests/{id}/media` | UC-03 |
| POST | `/api/requests/{id}/assess` | UC-03 |
| GET | `/api/requests/{id}` | Shared |
| GET | `/api/requests?clientId={id}` | Shared |
| GET | `/api/requests?technicianId={id}` | Shared |
| GET | `/api/technicians/recommended?requestId={id}` | UC-02 |
| GET | `/api/technicians/{id}/slots` | UC-02 |
| POST | `/api/appointments` | UC-02 |
| PATCH | `/api/appointments/{id}/accept` | UC-02 |
| PATCH | `/api/appointments/{id}/cancel` | UC-02 |
| PATCH | `/api/requests/{id}/status` | UC-01 |
| GET | `/api/notifications?recipientId={id}` | UC-01 |
| GET | `/api/history?clientId={id}` | UC-01 |

## Shared decisions
- Database status values are uppercase `PENDING`, `ASSIGNED`, `IN_PROGRESS`, `COMPLETED`. UI may show friendly labels.
- Appointments: `PENDING`, `ACCEPTED`, `CANCELLED`.
- User roles: `CLIENT`, `TECHNICIAN`, `ADMIN`.
- Dates/times: use ISO-8601 JSON formats; agree on timezone (recommend UTC for timestamps).
- `password_hash` must contain a secure hash, never plaintext.
- Authentication/authorization and role enforcement **are not implemented**. Do not expose the prototype publicly until secured.
- The schema does not yet include `MaintenanceCompany`; add it only after confirming Phase 4 requirements.
- Enforce booking conflicts, valid status transitions, history creation and notification dispatch **in transactional service code**. The schema alone does not enforce them.
- File storage should save a server-generated path, not trust client-provided paths. Validate file size/type.

## Integration checklist
1. Agree on JSON DTOs, response codes and errors.
2. Share the schema and use one development database schema definition (each developer can run a local MySQL instance).
3. Implement common entities once, then build UC-03 → UC-02 → UC-01.
4. Commit on `feature/uc03-report`, `feature/uc02-booking`, `feature/uc01-tracking`; merge through pull requests.
5. Add integration tests and sample data before demo.
