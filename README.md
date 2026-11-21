# SmartFix AI – Intelligent Maintenance Management System

 1. Project Title
SmartFix AI – Intelligent Maintenance Management System

2. Team Members
- Member 1: Fatima Alyafei – Update and Track Maintenance Request (UC-01)
- Member 2: Madiha Sameen – Find Technician and Book Appointment (UC-02)
- Member 3: Maitha Alkhyeli – Report Maintenance Issue and AI Assessment (UC-03)

3. Problem Description
Maintenance problems such as broken air conditioners, water leaks, and damaged equipment can take a long time to report and repair. Users may also have difficulty finding available technicians and tracking the progress of their requests.

SmartFix AI aims to solve these problems by providing one platform where clients can report maintenance issues, book technicians, and follow repair progress.

4. Key Features
SmartFix AI is designed around three main use cases:

UC-01: Update and Track Maintenance Request
- Technicians can view assigned maintenance requests.
- Technicians can update request statuses.
- Clients can track their maintenance requests.
- The system supports notifications and maintenance history.

UC-02: Find Technician and Book Appointment
- Clients can search for available technicians.
- Clients can select technicians and book appointments.
- Technicians can accept appointment requests.
- Clients can receive booking confirmations.

UC-03: Report Maintenance Issue and AI Assessment
- Clients can report maintenance problems.
- Clients can provide descriptions and supporting media.
- The system is designed to classify maintenance issues.
- The system determines issue severity and priority through its assessment component.

5. Technologies Used
- Frontend: HTML, CSS, JavaScript
- **Backend:** Java 17, Spring Boot
- Database: MySQL
- Database Tool: MySQL Workbench
- Development Environment: Visual Studio Code
- Build Tool: Apache Maven
- Version Control: Git and GitHub

 6. Prerequisites
Before running the project, install:

- Java Development Kit (JDK) 17 or later
- Apache Maven 3.9 or later
- MySQL Server 8.0 or later
- MySQL Workbench
- Visual Studio Code
- A modern web browser such as Google Chrome

7. Installation
1. Download or clone the SmartFix AI GitHub repository.
2. Open the project folder in Visual Studio Code.
3. Open MySQL Workbench and connect to your local MySQL server.
4. Open `database/schema.sql` and execute the SQL script.
5. Confirm that the `smartfix_ai` database and its tables were created.
6. Install the required Java and Spring Boot dependencies using Maven.

8. Configuration
The backend configuration is stored in:

`src/main/resources/application.properties`

The project uses the following database connection settings:

```properties
spring.application.name=smartfix-ai
server.port=8081

spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/smartfix_ai}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:}

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false
```

Set `DB_PASSWORD` to your local MySQL password using an environment variable. Do not upload real passwords or private credentials to GitHub.

9. How to Run
1. Start MySQL Server.
2. Open the SmartFix AI project in Visual Studio Code.
3. Open the terminal in the project root folder.
4. Run the Spring Boot application:

```bash
mvn spring-boot:run
```

5. Wait for the terminal to show that the application has started on port `8081`.
6. Open `frontend/index.html` using the Live Server extension in VS Code.
7. Use the SmartFix AI web interface to access the available maintenance features.

The backend API base URL is:

`http://localhost:8081/api`

10. How to Test
The project can be tested using the following steps:

Test 1: Database Connection
1. Start the Spring Boot application.
2. Check that the application connects to MySQL without database errors.

Test 2: View Assigned Maintenance Requests
1. Open the Technician Dashboard.
2. Click **View Assigned Requests**.
3. Check that requests assigned to the technician are displayed.

Test 3: Update Maintenance Request Status
1. Select an assigned maintenance request.
2. Change its status to In Progress or Completed.
3. Verify that the new status is saved in MySQL.

Test 4: Track Maintenance Request
1. Enter a valid maintenance request ID.
2. Click **Track Request**.
3. Verify that the current request status is displayed.

Test 5: Invalid Request ID
1. Enter a request ID that does not exist.
2. Click **Track Request**.
3. Verify that the system displays an appropriate error message.

Test 6: Backend Automated Tests

Run the following command in the project terminal:

```bash
mvn test
```

Review the test results and correct any failed tests.

