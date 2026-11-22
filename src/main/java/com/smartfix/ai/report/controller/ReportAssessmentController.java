package com.smartfix.ai.report.controller;

import com.smartfix.ai.report.dto.IssueAssessmentResponse;
import com.smartfix.ai.report.dto.IssueReportRequest;
import com.smartfix.ai.report.service.ReportAssessmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportAssessmentController {

    private final ReportAssessmentService assessmentService;

    public ReportAssessmentController(ReportAssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @PostMapping("/assess")
    public ResponseEntity<IssueAssessmentResponse> submitReport(@RequestBody IssueReportRequest request) {
        IssueAssessmentResponse response = assessmentService.submitAndAssess(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity> handleBadInput(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
}
