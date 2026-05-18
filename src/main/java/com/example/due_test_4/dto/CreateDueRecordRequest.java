package com.example.due_test_4.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Builder()
@Data()
public class CreateDueRecordRequest {

    private String studentId;

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public CreateDueRecordRequest() {
    }

    public CreateDueRecordRequest(String studentId, String dueType, Double amount, String remarks, String status, LocalDate dueDate) {
        this.studentId = studentId;
        this.dueType = dueType;
        this.amount = amount;
        this.remarks = remarks;
        this.status = status;
        this.dueDate = dueDate;
    }
}
