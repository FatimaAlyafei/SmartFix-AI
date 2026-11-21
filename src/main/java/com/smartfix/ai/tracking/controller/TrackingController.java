package com.smartfix.ai.tracking.controller;

import com.smartfix.ai.common.entity.MaintenanceRequest;
import com.smartfix.ai.tracking.dto.StatusUpdateRequest;
import com.smartfix.ai.tracking.entity.*;
import com.smartfix.ai.tracking.service.TrackingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@RestController @RequestMapping("/api")
public class TrackingController {
 private final TrackingService service;
 public TrackingController(TrackingService service){this.service=service;}
 // Shared read endpoints: retain ONE implementation when merging with UC-03.
 @GetMapping("/requests")
 public List<MaintenanceRequest> requests(@RequestParam(required=false) Long technicianId,@RequestParam(required=false) Long clientId){
  if((technicianId==null)==(clientId==null)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Provide exactly one of technicianId or clientId");
  return technicianId!=null?service.assigned(technicianId):service.clientRequests(clientId);
 }
 @GetMapping("/requests/{id}") public MaintenanceRequest details(@PathVariable Long id){return service.details(id);}
 // Demo-only technicianId query parameter. Replace with authenticated principal before production.
 @PatchMapping("/requests/{id}/status")
 public MaintenanceRequest update(@PathVariable Long id,@RequestParam Long technicianId,@Valid @RequestBody StatusUpdateRequest body){return service.update(id,technicianId,body);}
 @GetMapping("/notifications") public List<Notification> notifications(@RequestParam Long recipientId){return service.notifications(recipientId);}
 @GetMapping("/history") public List<MaintenanceHistory> history(@RequestParam Long clientId){return service.history(clientId);}
}
