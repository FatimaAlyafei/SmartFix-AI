# SmartFix AI – Intelligent Maintenance Management System

## Project Overview
SmartFix AI is a web-based maintenance management system designed to help users report maintenance problems, find technicians, book appointments, and track repair progress.

## Main Features
1. **Report Maintenance Issues:** Clients can submit maintenance problems with descriptions and supporting media.
2. **AI Issue Assessment:** The system is designed to classify maintenance issues and determine their priority.
3. **Find Technician and Book Appointment:** Clients can select technicians and book available appointments.
4. **Update and Track Maintenance Requests:** Technicians can update repair statuses, and clients can track their requests.
5. **Notifications and Maintenance History:** The system supports notifications and records of completed repairs.

## Technologies Used
- Frontend: HTML, CSS, JavaScript
- Backend: Java, Spring Boot
- Database: MySQL
- Development Tools: Visual Studio Code, MySQL Workbench
- Version Control: GitHub

## Project Structure
- `src/main/java/` – Java backend source code
- `src/main/resources/` – Application configuration
- `frontend/` – Web interface
- `database/` – MySQL database scripts
- `src/test/` – Backend tests
- `pom.xml` – Maven project configuration

## How to Run
1. Install Java JDK 17 or later, Maven, and MySQL.
2. Open the project in Visual Studio Code.
3. Execute `database/schema.sql` in MySQL Workbench.
4. Configure the database connection in `application.properties` using local environment variables.
5. Start the Spring Boot backend using `mvn spring-boot:run`.
6. Open the frontend using a local development server.

## Development Progress
The project is being developed in three use cases:

- **UC-01:** Update and Track Maintenance Request
- **UC-02:** Find Technician and Book Appointment
- **UC-03:** Report Maintenance Issue and AI Assessment

The modules are being implemented and integrated using a shared database and backend architecture.

## Testing
The project includes backend testing and will be tested using functional test cases to verify maintenance request operations, booking, tracking, and status updates.

## Team Contributions
Each team member is responsible for implementing and testing their assigned use case. All modules will be integrated into one SmartFix AI application.
