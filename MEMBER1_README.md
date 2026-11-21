# Member 1 — UC-01 Update & Track Maintenance Request

This is the original shared starter project **plus** Member 1's tracking implementation. The `report/` and `booking/` folders remain placeholders for Members 3 and 2.

## What is implemented
- List technician's assigned requests and client requests
- Read request details
- Update `ASSIGNED -> IN_PROGRESS -> COMPLETED` (the strict two-step workflow)
- Notify client in the `notifications` table on each successful change
- Create one maintenance-history row on completion, with a required resolution
- Reject missing requests, unauthorized technician IDs, and invalid status transitions
- Client and technician HTML/JavaScript pages
- Five unit tests (Mockito/JUnit)

## How to run
1. Install Java 17+, Maven 3.9+, MySQL Server and VS Code Java extensions.
2. Execute `database/schema.sql` in MySQL Workbench (this creates all 9 shared tables).
3. Set `DB_USER` and `DB_PASSWORD` environment variables (see `application.properties`).
4. Run `mvn test` then `mvn spring-boot:run` from this folder.
5. Open `http://localhost:8080/assigned-requests.html` or `/track-request.html`.
6. Add test records using `database/demo_uc01.sql` (demo only, not production).

## API
- `GET /api/requests?technicianId=2`
- `GET /api/requests?clientId=1`
- `GET /api/requests/1`
- `PATCH /api/requests/1/status?technicianId=2` JSON: `{"status":"IN_PROGRESS"}`
- `PATCH /api/requests/1/status?technicianId=2` JSON: `{"status":"COMPLETED","resolution":"Fixed AC","totalCost":100.00}` (only after IN_PROGRESS)
- `GET /api/notifications?recipientId=1`
- `GET /api/history?clientId=1`

## Integration notes — important
- `MaintenanceRequest` and `MaintenanceRequestRepository` are shared under `common/`. Members 2/3 must **reuse**, not recreate them.
- The `GET /api/requests` and `GET /api/requests/{id}` mappings are currently in `TrackingController`. When merging, keep only ONE implementation of each route; Member 3's POST routes can coexist in a separate controller.
- The MySQL schema uses uppercase values (`PENDING`, `ASSIGNED`, `IN_PROGRESS`, `COMPLETED`), even though UI labels have spaces.
- This is a university demonstration, **not a secure deployed app**: technician/client IDs are supplied by the browser and there is no login/authorization middleware. Replace these IDs with authenticated user identity and add role/ownership checks before deployment.
- Notifications are **database/in-app records**, not email or push notifications.
- The Spring Boot backend and HTML pages run on the same port; no extra frontend server needed.
- `spring.jpa.hibernate.ddl-auto=validate` means the provided SQL schema must be executed first.
- SQL database integration was not run in this environment. Run the tests and manual scenarios on your machine before taking evidence screenshots.

## Screenshots to collect
1. Technician list showing request #1 assigned to technician #2.
2. Successful update to IN_PROGRESS and client's notification/status page.
3. Successful completion and new maintenance history row.
4. Optional: invalid transition or wrong technician error message.

## GitHub
Create branch `member1-uc01-tracking`, commit your tracking and shared files, and open a pull request to merge. Do not upload MySQL passwords or local `.env` files.
