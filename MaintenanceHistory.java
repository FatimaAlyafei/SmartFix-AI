package com.smartfix.ai.tracking.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="maintenance_history")
public class MaintenanceHistory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="history_id") private Long historyId;
 @Column(name="request_id",nullable=false,unique=true) private Long requestId;
 @Column(name="completion_date",nullable=false) private LocalDateTime completionDate;
 private String resolution;
 @Column(name="total_cost") private BigDecimal totalCost;
 protected MaintenanceHistory(){}
 public MaintenanceHistory(Long requestId,String resolution,BigDecimal totalCost){this.requestId=requestId;this.resolution=resolution;this.totalCost=totalCost;this.completionDate=LocalDateTime.now();}
 public Long getHistoryId(){return historyId;} public Long getRequestId(){return requestId;}
 public LocalDateTime getCompletionDate(){return completionDate;} public String getResolution(){return resolution;}
 public BigDecimal getTotalCost(){return totalCost;}
}
