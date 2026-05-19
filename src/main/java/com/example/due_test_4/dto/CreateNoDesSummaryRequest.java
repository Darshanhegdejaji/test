package com.example.due_test_4.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Builder()
@Data()
public class CreateNoDesSummaryRequest {

    private String documentUrl;

    public String getDocumentUrl() {
        return this.documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    private LocalDateTime generatedAt;

    public LocalDateTime getGeneratedAt() {
        return this.generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    private LocalDateTime expiresAt;

    public LocalDateTime getExpiresAt() {
        return this.expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public CreateNoDesSummaryRequest() {
    }

    public CreateNoDesSummaryRequest(String documentUrl, LocalDateTime generatedAt, LocalDateTime expiresAt) {
        this.documentUrl = documentUrl;
        this.generatedAt = generatedAt;
        this.expiresAt = expiresAt;
    }
}
