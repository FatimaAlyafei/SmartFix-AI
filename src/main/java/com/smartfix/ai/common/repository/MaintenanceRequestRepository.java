package com.smartfix.ai.common.repository;


import com.smartfix.ai.common.entity.MaintenanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest,Long> {
 List<MaintenanceRequest> findByTechnicianIdOrderByCreatedAtDesc(Long technicianId);
 List<MaintenanceRequest> findByClientIdOrderByCreatedAtDesc(Long clientId);
}
