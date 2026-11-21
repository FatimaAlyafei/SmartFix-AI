USE smartfix_ai;
INSERT INTO users(user_id,name,email,password_hash,role) VALUES
(1,'Demo Client','client-demo@smartfix.test','DEMO-NOT-A-REAL-PASSWORD','CLIENT'),
(2,'Demo Technician','technician-demo@smartfix.test','DEMO-NOT-A-REAL-PASSWORD','TECHNICIAN')
ON DUPLICATE KEY UPDATE name=VALUES(name);
INSERT INTO clients(client_id,address) VALUES (1,'Abu Dhabi') ON DUPLICATE KEY UPDATE address=VALUES(address);
INSERT INTO technicians(technician_id,specialization,skills,location,availability) VALUES (2,'AC','Air conditioning','Abu Dhabi','Available') ON DUPLICATE KEY UPDATE specialization=VALUES(specialization);
INSERT INTO maintenance_requests(request_id,client_id,technician_id,description,issue_category,severity_level,priority_level,status,location)
VALUES(1,1,2,'AC not cooling','AC','MEDIUM','MEDIUM','ASSIGNED','Abu Dhabi')
ON DUPLICATE KEY UPDATE description=VALUES(description);
