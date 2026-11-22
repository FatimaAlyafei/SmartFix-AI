package com.smartfix.ai.report.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class ReportAlertRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "request_id", nullable = false)
    private Long requestId;

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "sent_at", insertable = false, updatable = false)
    private LocalDateTime sentAt;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    public Long getNotificationId() { return notificationId; }
    public Long getRequestId() { return requestId; }
    public Long getRecipientId() { return recipientId; }
    public String getMessage() { return message; }
    public String getType() { return type; }
    public LocalDateTime getSentAt() { return sentAt; }
    public Boolean getIsRead() { return isRead; }

    public void setNotificationId(Long notificationId) { this.notificationId = notificationId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }
    public void setMessage(String message) { this.message = message; }
    public void setType(String type) { this.type = type; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
}
