package com.smartfix.ai.report.repository;

import com.smartfix.ai.common.entity.MaintenanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {
}
