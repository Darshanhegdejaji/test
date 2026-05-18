package com.example.due_test_4.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Builder()
@Data()
public class ClearanceRecordDTO {

    private String id;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String overallStatus;

    public String getOverallStatus() {
        return this.overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }

    private Integer totalDues;

    public Integer getTotalDues() {
        return this.totalDues;
    }

    public void setTotalDues(Integer totalDues) {
        this.totalDues = totalDues;
    }

    private Integer clearedDues;

    public Integer getClearedDues() {
        return this.clearedDues;
    }

    public void setClearedDues(Integer clearedDues) {
        this.clearedDues = clearedDues;
    }

    private Integer pendingDues;

    public Integer getPendingDues() {
        return this.pendingDues;
    }

    public void setPendingDues(Integer pendingDues) {
        this.pendingDues = pendingDues;
    }

    private LocalDateTime approvedAt;

    public LocalDateTime getApprovedAt() {
        return this.approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private LocalDateTime updatedAt;

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ClearanceRecordDTO() {
    }

    public ClearanceRecordDTO(String id, String overallStatus, Integer totalDues, Integer clearedDues, Integer pendingDues, LocalDateTime approvedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.overallStatus = overallStatus;
        this.totalDues = totalDues;
        this.clearedDues = clearedDues;
        this.pendingDues = pendingDues;
        this.approvedAt = approvedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
