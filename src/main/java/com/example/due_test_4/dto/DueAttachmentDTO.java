package com.example.due_test_4.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Builder()
@Data()
public class DueAttachmentDTO {

    private String id;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String fileName;

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String fileUrl;

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    private String fileType;

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    private LocalDateTime uploadedAt;

    public LocalDateTime getUploadedAt() {
        return this.uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public DueAttachmentDTO() {
    }

    public DueAttachmentDTO(String id, String fileName, String fileUrl, String fileType, LocalDateTime uploadedAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.uploadedAt = uploadedAt;
    }
}
