package com.smartfix.ai.report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "media_files")
public class AttachmentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Long mediaId;

    @Column(name = "request_id", nullable = false)
    private Long requestId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    public boolean isValidMedia() {
        boolean hasName = fileName != null && !fileName.isBlank();
        boolean validMime = fileType != null && (
                fileType.startsWith("image/") ||
                fileType.startsWith("video/") ||
                fileType.startsWith("audio/")
        );
        return hasName && validMime;
    }

    public Long getMediaId() { return mediaId; }
    public Long getRequestId() { return requestId; }
    public String getFileName() { return fileName; }
    public String getFileType() { return fileType; }
    public String getFilePath() { return filePath; }

    public void setMediaId(Long mediaId) { this.mediaId = mediaId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}
