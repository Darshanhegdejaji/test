package com.example.due_test_4.dto;

import lombok.Builder;
import lombok.Data;

@Builder()
@Data()
public class AddDueAttachmentRequest {

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

    public AddDueAttachmentRequest() {
    }

    public AddDueAttachmentRequest(String fileName, String fileUrl, String fileType) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
    }
}
