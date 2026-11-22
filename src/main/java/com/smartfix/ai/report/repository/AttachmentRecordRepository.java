package com.smartfix.ai.report.repository;

import com.smartfix.ai.report.entity.AttachmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRecordRepository extends JpaRepository<AttachmentRecord, Long> {
}
