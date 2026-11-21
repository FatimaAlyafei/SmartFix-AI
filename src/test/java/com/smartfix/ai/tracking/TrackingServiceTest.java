package com.smartfix.ai.tracking;
import com.smartfix.ai.common.entity.MaintenanceRequest;
import com.smartfix.ai.common.repository.MaintenanceRequestRepository;
import com.smartfix.ai.tracking.dto.StatusUpdateRequest;
import com.smartfix.ai.tracking.repository.*;
import com.smartfix.ai.tracking.service.TrackingService;
import org.junit.jupiter.api.*;
import org.springframework.web.server.ResponseStatusException;
import java.lang.reflect.Field;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class TrackingServiceTest {
 MaintenanceRequestRepository requests=mock(MaintenanceRequestRepository.class);
 NotificationRepository notifications=mock(NotificationRepository.class);
 MaintenanceHistoryRepository histories=mock(MaintenanceHistoryRepository.class);
 TrackingService service=new TrackingService(requests,notifications,histories);
 MaintenanceRequest request(String status) throws Exception {MaintenanceRequest r=new MaintenanceRequest();for(var entry:new Object[][]{{"requestId",1L},{"clientId",1L},{"technicianId",2L},{"status",status}}){Field f=MaintenanceRequest.class.getDeclaredField((String)entry[0]);f.setAccessible(true);f.set(r,entry[1]);}when(requests.findById(1L)).thenReturn(Optional.of(r));when(requests.save(any())).thenAnswer(i->i.getArgument(0));return r;}
 @Test void inProgressNotifiesClient() throws Exception {request("ASSIGNED");assertEquals("IN_PROGRESS",service.update(1L,2L,new StatusUpdateRequest("In Progress",null,null)).getStatus());verify(notifications).save(any());verify(histories,never()).save(any());}
 @Test void completedCreatesHistory() throws Exception {request("IN_PROGRESS");assertEquals("COMPLETED",service.update(1L,2L,new StatusUpdateRequest("Completed","Replaced AC filter",null)).getStatus());verify(notifications).save(any());verify(histories).save(any());}
 @Test void wrongTechnicianRejected() throws Exception {request("ASSIGNED");assertThrows(ResponseStatusException.class,()->service.update(1L,99L,new StatusUpdateRequest("IN_PROGRESS",null,null)));verifyNoInteractions(notifications,histories);}
 @Test void invalidTransitionRejected() throws Exception {request("ASSIGNED");assertThrows(ResponseStatusException.class,()->service.update(1L,2L,new StatusUpdateRequest("COMPLETED","Fixed",null)));verifyNoInteractions(notifications,histories);}
 @Test void missingRequestRejected(){when(requests.findById(88L)).thenReturn(Optional.empty());assertThrows(ResponseStatusException.class,()->service.details(88L));}
}
