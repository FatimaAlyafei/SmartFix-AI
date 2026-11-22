package com.smartfix.ai.report.service;

import com.smartfix.ai.common.entity.MaintenanceRequest;
import com.smartfix.ai.report.dto.IssueAssessmentResponse;
import com.smartfix.ai.report.dto.IssueReportRequest;
import com.smartfix.ai.report.entity.AttachmentRecord;
import com.smartfix.ai.report.entity.ReportAlertRecord;
import com.smartfix.ai.report.repository.AttachmentRecordRepository;
import com.smartfix.ai.report.repository.MaintenanceRequestRepository;
import com.smartfix.ai.report.repository.ReportAlertRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportAssessmentServiceTest {

    @Mock
    private MaintenanceRequestRepository requestRepository;

    @Mock
    private AttachmentRecordRepository attachmentRepository;

    @Mock
    private ReportAlertRecordRepository alertRepository;

    private ReportAssessmentService service;

    @BeforeEach
    void setUp() {
        service = new ReportAssessmentService(requestRepository, attachmentRepository, alertRepository);
    }

    @Test
    void submitAndAssess_validWaterLeakReport_classifiesAndSavesRecords() {
        IssueReportRequest request = new IssueReportRequest();
        request.setClientId(10L);
        request.setDescription("Water leak under kitchen sink");
        request.setLocation("Building B, Room 204");
        request.setFileName("leak.jpg");
        request.setFileType("image/jpeg");
        request.setFilePath("/uploads/leak.jpg");

        when(requestRepository.save(any(MaintenanceRequest.class))).thenAnswer(invocation -> {
            MaintenanceRequest saved = invocation.getArgument(0);
            setField(saved, "requestId", 101L);
            return saved;
        });

        when(attachmentRepository.save(any(AttachmentRecord.class))).thenAnswer(invocation -> {
            AttachmentRecord saved = invocation.getArgument(0);
            saved.setMediaId(201L);
            return saved;
        });

        when(alertRepository.save(any(ReportAlertRecord.class))).thenAnswer(invocation -> {
            ReportAlertRecord saved = invocation.getArgument(0);
            saved.setNotificationId(301L);
            return saved;
        });

        IssueAssessmentResponse response = service.submitAndAssess(request);

        assertEquals(101L, response.getRequestId());
        assertEquals(201L, response.getMediaId());
        assertEquals(301L, response.getNotificationId());
        assertEquals("Plumbing", response.getIssueCategory());
        assertEquals("MEDIUM", response.getSeverityLevel());
        assertEquals("MEDIUM", response.getPriorityLevel());
        assertEquals("PENDING", response.getStatus());

        verify(requestRepository).save(any(MaintenanceRequest.class));
        verify(attachmentRepository).save(any(AttachmentRecord.class));
        verify(alertRepository).save(any(ReportAlertRecord.class));
    }

    @Test
    void submitAndAssess_invalidMediaType_throwsIllegalArgumentException() {
        IssueReportRequest request = new IssueReportRequest();
        request.setClientId(10L);
        request.setDescription("Broken light");
        request.setLocation("Hallway");
        request.setFileName("script.exe");
        request.setFileType("application/x-msdownload");

        assertThrows(IllegalArgumentException.class, () -> service.submitAndAssess(request));
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
