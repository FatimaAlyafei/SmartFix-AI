package com.smartfix.ai.report.dto;

public class IssueAssessmentResponse {

    private Long requestId;
    private Long mediaId;
    private Long notificationId;
    private String issueCategory;
    private String severityLevel;
    private String priorityLevel;
    private String status;
    private String summaryMessage;

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public Long getMediaId() { return mediaId; }
    public void setMediaId(Long mediaId) { this.mediaId = mediaId; }

    public Long getNotificationId() { return notificationId; }
    public void setNotificationId(Long notificationId) { this.notificationId = notificationId; }

    public String getIssueCategory() { return issueCategory; }
    public void setIssueCategory(String issueCategory) { this.issueCategory = issueCategory; }

    public String getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }

    public String getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSummaryMessage() { return summaryMessage; }
    public void setSummaryMessage(String summaryMessage) { this.summaryMessage = summaryMessage; }
}
