package com.smartfix.ai.tracking.repository;


import com.smartfix.ai.tracking.entity.MaintenanceHistory;
import org.springframework.data.jpa.repository.*;
import com.smartfix.ai.common.entity.MaintenanceRequest;
import org.springframework.data.repository.query.Param;
import java.util.*;
public interface MaintenanceHistoryRepository extends JpaRepository<MaintenanceHistory,Long>{
 boolean existsByRequestId(Long requestId);
 @Query("select h from MaintenanceHistory h, MaintenanceRequest r where h.requestId=r.requestId and r.clientId=:clientId order by h.completionDate desc")
 List<MaintenanceHistory> findByClientId(@Param("clientId") Long clientId);
}
