CREATE DATABASE IF NOT EXISTS smartfix_ai CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smartfix_ai;

CREATE TABLE IF NOT EXISTS users (
 user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(120) NOT NULL,
 email VARCHAR(190) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL,
 role ENUM('CLIENT','TECHNICIAN','ADMIN') NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS clients (
 client_id BIGINT PRIMARY KEY,
 address VARCHAR(255),
 CONSTRAINT fk_client_user FOREIGN KEY (client_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS technicians (
 technician_id BIGINT PRIMARY KEY,
 specialization VARCHAR(120) NOT NULL,
 skills TEXT,
 location VARCHAR(255),
 availability VARCHAR(60),
 rating DECIMAL(3,2) DEFAULT 0.00,
 workload INT NOT NULL DEFAULT 0,
 CONSTRAINT ck_technician_rating CHECK (rating BETWEEN 0 AND 5),
 CONSTRAINT ck_technician_workload CHECK (workload >= 0),
 CONSTRAINT fk_technician_user FOREIGN KEY (technician_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS maintenance_requests (
 request_id BIGINT PRIMARY KEY AUTO_INCREMENT,
 client_id BIGINT NOT NULL,
 technician_id BIGINT NULL,
 description TEXT NOT NULL,
 issue_category VARCHAR(100),
 severity_level ENUM('LOW','MEDIUM','HIGH','CRITICAL'),
 priority_level ENUM('LOW','MEDIUM','HIGH','CRITICAL'),
 status ENUM('PENDING','ASSIGNED','IN_PROGRESS','COMPLETED') NOT NULL DEFAULT 'PENDING',
 location VARCHAR(255) NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 CONSTRAINT fk_request_client FOREIGN KEY (client_id) REFERENCES clients(client_id),
 CONSTRAINT fk_request_technician FOREIGN KEY (technician_id) REFERENCES technicians(technician_id),
 INDEX idx_request_client_status (client_id,status),
 INDEX idx_request_technician_status (technician_id,status)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS media_files (
 media_id BIGINT PRIMARY KEY AUTO_INCREMENT,
 request_id BIGINT NOT NULL,
 file_name VARCHAR(255) NOT NULL,
 file_type VARCHAR(100) NOT NULL,
 file_path VARCHAR(500) NOT NULL,
 CONSTRAINT fk_media_request FOREIGN KEY (request_id) REFERENCES maintenance_requests(request_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS technician_availability (
 slot_id BIGINT PRIMARY KEY AUTO_INCREMENT,
 technician_id BIGINT NOT NULL,
 start_time DATETIME NOT NULL,
 end_time DATETIME NOT NULL,
 is_available BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT fk_slot_technician FOREIGN KEY (technician_id) REFERENCES technicians(technician_id),
 CONSTRAINT ck_slot_order CHECK (end_time > start_time),
 UNIQUE KEY uq_technician_slot (technician_id,start_time,end_time)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS appointments (
 appointment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
 request_id BIGINT NOT NULL,
 technician_id BIGINT NOT NULL,
 appointment_date DATE NOT NULL,
 appointment_time TIME NOT NULL,
 estimated_cost DECIMAL(10,2) NOT NULL DEFAULT 0,
 status ENUM('PENDING','ACCEPTED','CANCELLED') NOT NULL DEFAULT 'PENDING',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT fk_appointment_request FOREIGN KEY (request_id) REFERENCES maintenance_requests(request_id),
 CONSTRAINT fk_appointment_technician FOREIGN KEY (technician_id) REFERENCES technicians(technician_id),
 CONSTRAINT ck_estimated_cost CHECK (estimated_cost >= 0),
 INDEX idx_appointment_technician_date (technician_id,appointment_date,appointment_time)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS notifications (
 notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
 request_id BIGINT NOT NULL,
 recipient_id BIGINT NOT NULL,
 message TEXT NOT NULL,
 type VARCHAR(60) NOT NULL,
 sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 is_read BOOLEAN NOT NULL DEFAULT FALSE,
 CONSTRAINT fk_notification_request FOREIGN KEY (request_id) REFERENCES maintenance_requests(request_id),
 CONSTRAINT fk_notification_recipient FOREIGN KEY (recipient_id) REFERENCES users(user_id),
 INDEX idx_notification_recipient (recipient_id,is_read)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS maintenance_history (
 history_id BIGINT PRIMARY KEY AUTO_INCREMENT,
 request_id BIGINT NOT NULL UNIQUE,
 completion_date DATETIME NOT NULL,
 resolution TEXT,
 total_cost DECIMAL(10,2),
 CONSTRAINT fk_history_request FOREIGN KEY (request_id) REFERENCES maintenance_requests(request_id),
 CONSTRAINT ck_total_cost CHECK (total_cost IS NULL OR total_cost >= 0)
) ENGINE=InnoDB;
