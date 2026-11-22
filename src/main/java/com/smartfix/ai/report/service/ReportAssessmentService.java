package com.smartfix.ai.report.service;

import com.smartfix.ai.common.entity.MaintenanceRequest;
import com.smartfix.ai.report.dto.IssueAssessmentResponse;
import com.smartfix.ai.report.dto.IssueReportRequest;
import com.smartfix.ai.report.entity.AttachmentRecord;
import com.smartfix.ai.report.entity.ReportAlertRecord;
import com.smartfix.ai.report.repository.AttachmentRecordRepository;
import com.smartfix.ai.report.repository.MaintenanceRequestRepository;
import com.smartfix.ai.report.repository.ReportAlertRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class ReportAssessmentService {

    private final MaintenanceRequestRepository requestRepository;
    private final AttachmentRecordRepository attachmentRepository;
    private final ReportAlertRecordRepository alertRepository;

    public ReportAssessmentService(
            MaintenanceRequestRepository requestRepository,
            AttachmentRecordRepository attachmentRepository,
            ReportAlertRecordRepository alertRepository
    ) {
        this.requestRepository = requestRepository;
        this.attachmentRepository = attachmentRepository;
        this.alertRepository = alertRepository;
    }

    @Transactional
    public IssueAssessmentResponse submitAndAssess(IssueReportRequest input) {
        if (input == null) {
            throw new IllegalArgumentException("Report request cannot be null.");
        }
        if (input.getClientId() == null) {
            throw new IllegalArgumentException("Client ID is required.");
        }
        if (input.getDescription() == null || input.getDescription().isBlank()) {
            throw new IllegalArgumentException("Issue description is required.");
        }
        if (input.getLocation() == null || input.getLocation().isBlank()) {
            throw new IllegalArgumentException("Location is required.");
        }

        AttachmentRecord attachment = new AttachmentRecord();
        attachment.setFileName(input.getFileName());
        attachment.setFileType(input.getFileType());
        attachment.setFilePath(
                (input.getFilePath() == null || input.getFilePath().isBlank())
                        ? "/uploads/" + input.getFileName()
                        : input.getFilePath()
        );

        if (!attachment.isValidMedia()) {
            throw new IllegalArgumentException("Invalid or missing media file details.");
        }

        String normalizedText = (input.getDescription() + " " + input.getFileName()).toLowerCase(Locale.ROOT);
        String category = classifyIssue(normalizedText);
        String severity = determineSeverity(normalizedText, category);
        String priority = determinePriority(severity);

        MaintenanceRequest request = new MaintenanceRequest();
        request.setClientId(input.getClientId());
        request.setDescription(input.getDescription().trim());
        request.setLocation(input.getLocation().trim());
        request.setIssueCategory(category);
        request.setSeverityLevel(severity);
        request.setPriorityLevel(priority);
        request.setStatus("PENDING");

        MaintenanceRequest savedRequest = requestRepository.save(request);

        attachment.setRequestId(savedRequest.getRequestId());
        AttachmentRecord savedAttachment = attachmentRepository.save(attachment);

        ReportAlertRecord alert = new ReportAlertRecord();
        alert.setRequestId(savedRequest.getRequestId());
        alert.setRecipientId(input.getClientId());
        alert.setType("AI_ASSESSMENT_COMPLETE");
        alert.setMessage(
                "Request #" + savedRequest.getRequestId() +
                " classified as " + category +
                " with " + severity + " severity and " + priority + " priority."
        );
        alert.setIsRead(false);
        ReportAlertRecord savedAlert = alertRepository.save(alert);

        IssueAssessmentResponse response = new IssueAssessmentResponse();
        response.setRequestId(savedRequest.getRequestId());
        response.setMediaId(savedAttachment.getMediaId());
        response.setNotificationId(savedAlert.getNotificationId());
        response.setIssueCategory(category);
        response.setSeverityLevel(severity);
        response.setPriorityLevel(priority);
        response.setStatus(savedRequest.getStatus());
        response.setSummaryMessage(
                "Assessment completed for " + input.getLocation().trim() +
                " [" + category + " | Severity: " + severity + " | Priority: " + priority + "]."
        );
        return response;
    }

    public String classifyIssue(String text) {
        if (text.contains("leak") || text.contains("pipe") || text.contains("water") || text.contains("plumb") || text.contains("drain")) {
            return "Plumbing";
        }
        if (text.contains("spark") || text.contains("wire") || text.contains("power") || text.contains("outlet") || text.contains("electric") || text.contains("breaker")) {
            return "Electrical";
        }
        if (text.contains("ac") || text.contains("hvac") || text.contains("air condition") || text.contains("heat") || text.contains("cool")) {
            return "HVAC";
        }
        if (text.contains("lock") || text.contains("door") || text.contains("window") || text.contains("crack") || text.contains("wall")) {
            return "Structural & Access";
        }
        return "General Maintenance";
    }

    public String determineSeverity(String text, String category) {
        if (text.contains("flood") || text.contains("fire") || text.contains("spark") || text.contains("gas") || text.contains("burst") || text.contains("hazard")) {
            return "CRITICAL";
        }
        if (text.contains("major") || text.contains("broken") || text.contains("no power") || text.contains("heavy leak") || "Electrical".equals(category)) {
            return "HIGH";
        }
        if (text.contains("leak") || text.contains("drip") || text.contains("ac") || text.contains("noise") || text.contains("slow")) {
            return "MEDIUM";
        }
        return "LOW";
    }

    public String determinePriority(String severity) {
        return switch (severity) {
            case "CRITICAL" -> "CRITICAL";
            case "HIGH" -> "HIGH";
            case "MEDIUM" -> "MEDIUM";
            default -> "LOW";
        };
    }
}
