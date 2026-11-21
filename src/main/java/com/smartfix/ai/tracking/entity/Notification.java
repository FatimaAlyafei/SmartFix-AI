package com.smartfix.ai.tracking.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="notifications")
public class Notification {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="notification_id") private Long notificationId;
 @Column(name="request_id",nullable=false) private Long requestId;
 @Column(name="recipient_id",nullable=false) private Long recipientId;
 @Column(nullable=false) private String message;
 @Column(nullable=false) private String type;
 @Column(name="sent_at",insertable=false,updatable=false) private LocalDateTime sentAt;
 @Column(name="is_read",nullable=false) private boolean read;
 protected Notification(){}
 public Notification(Long requestId,Long recipientId,String message){this.requestId=requestId;this.recipientId=recipientId;this.message=message;this.type="STATUS_UPDATE";}
 public Long getNotificationId(){return notificationId;} public Long getRequestId(){return requestId;}
 public Long getRecipientId(){return recipientId;} public String getMessage(){return message;}
 public String getType(){return type;} public LocalDateTime getSentAt(){return sentAt;}
 public boolean isRead(){return read;}
}
