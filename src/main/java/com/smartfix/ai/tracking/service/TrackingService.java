package com.smartfix.ai.tracking.service;


import com.smartfix.ai.common.entity.MaintenanceRequest;
import com.smartfix.ai.common.repository.MaintenanceRequestRepository;
import com.smartfix.ai.tracking.dto.StatusUpdateRequest;
import com.smartfix.ai.tracking.entity.*;
import com.smartfix.ai.tracking.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;
@Service
public class TrackingService {
 private final MaintenanceRequestRepository requests;
 private final NotificationRepository notifications;
 private final MaintenanceHistoryRepository histories;
 public TrackingService(MaintenanceRequestRepository requests,NotificationRepository notifications,MaintenanceHistoryRepository histories){this.requests=requests;this.notifications=notifications;this.histories=histories;}
 public List<MaintenanceRequest> assigned(Long technicianId){return requests.findByTechnicianIdOrderByCreatedAtDesc(technicianId);}
 public List<MaintenanceRequest> clientRequests(Long clientId){return requests.findByClientIdOrderByCreatedAtDesc(clientId);}
 public MaintenanceRequest details(Long requestId){return requests.findById(requestId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Maintenance request not found"));}
 public List<Notification> notifications(Long recipientId){return notifications.findByRecipientIdOrderBySentAtDesc(recipientId);}
 public List<MaintenanceHistory> history(Long clientId){return histories.findByClientId(clientId);}
 @Transactional
 public MaintenanceRequest update(Long requestId,Long technicianId,StatusUpdateRequest change){
  MaintenanceRequest request=details(requestId);
  if(request.getTechnicianId()==null || !request.getTechnicianId().equals(technicianId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Request is not assigned to this technician");
  String current=request.getStatus(); String next=change.status().trim().toUpperCase().replace(' ','_');
  if(!next.equals("IN_PROGRESS")&&!next.equals("COMPLETED")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only IN_PROGRESS or COMPLETED is allowed");
  boolean valid=(current.equals("ASSIGNED")&&next.equals("IN_PROGRESS"))||(current.equals("IN_PROGRESS")&&next.equals("COMPLETED"));
  if(!valid) throw new ResponseStatusException(HttpStatus.CONFLICT,"Invalid status transition from "+current+" to "+next);
  if(next.equals("COMPLETED") && (change.resolution()==null || change.resolution().isBlank())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Resolution is required for completed repairs");
  request.setStatus(next);
  MaintenanceRequest saved=requests.save(request);
  notifications.save(new Notification(requestId,request.getClientId(),"Maintenance request #"+requestId+" status changed to "+next.replace('_',' ')));
  if(next.equals("COMPLETED") && !histories.existsByRequestId(requestId)) histories.save(new MaintenanceHistory(requestId,change.resolution(),change.totalCost()));
  return saved;
 }
}
