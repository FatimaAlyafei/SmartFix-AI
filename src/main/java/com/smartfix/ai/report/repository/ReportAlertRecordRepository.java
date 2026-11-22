package com.smartfix.ai.report.repository;

import com.smartfix.ai.report.entity.ReportAlertRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportAlertRecordRepository extends JpaRepository<ReportAlertRecord, Long> {
}
