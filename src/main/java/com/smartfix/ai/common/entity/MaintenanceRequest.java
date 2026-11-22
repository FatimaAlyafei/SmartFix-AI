package com.smartfix.ai.common.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_requests")
public class MaintenanceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "technician_id")
    private Long technicianId;

    @Column(nullable = false)
    private String description;

    @Column(name = "issue_category")
    private String issueCategory;

    @Column(name = "severity_level")
    private String severityLevel;

    @Column(name = "priority_level")
    private String priorityLevel;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String location;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public Long getRequestId() { return requestId; }
    public Long getClientId() { return clientId; }
    public Long getTechnicianId() { return technicianId; }
    public String getDescription() { return description; }
    public String getIssueCategory() { return issueCategory; }
    public String getSeverityLevel() { return severityLevel; }
    public String getPriorityLevel() { return priorityLevel; }
    public String getStatus() { return status; }
    public String getLocation() { return location; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setClientId(Long clientId) { this.clientId = clientId; }
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }
    public void setDescription(String description) { this.description = description; }
    public void setIssueCategory(String issueCategory) { this.issueCategory = issueCategory; }
    public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }
    public void setPriorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; }
    public void setStatus(String value) { status = value; }
    public void setLocation(String location) { this.location = location; }
}
