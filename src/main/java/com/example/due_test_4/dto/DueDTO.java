package com.example.due_test_4.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder()
@Data()
public class DueDTO {

    private String id;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String dueType;

    public String getDueType() {
        return this.dueType;
    }

    public void setDueType(String dueType) {
        this.dueType = dueType;
    }

    private Double amount;

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    private String remarks;

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private LocalDate dueDate;

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    private LocalDate clearedDate;

    public LocalDate getClearedDate() {
        return this.clearedDate;
    }

    public void setClearedDate(LocalDate clearedDate) {
        this.clearedDate = clearedDate;
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

    public DueDTO() {
    }

    public DueDTO(String id, String dueType, Double amount, String remarks, String status, LocalDate dueDate, LocalDate clearedDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.dueType = dueType;
        this.amount = amount;
        this.remarks = remarks;
        this.status = status;
        this.dueDate = dueDate;
        this.clearedDate = clearedDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
