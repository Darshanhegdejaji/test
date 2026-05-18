package com.example.due_test_4.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Builder()
@Data()
public class UpdateDueRequest {

    private String remarks;

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    private Double amount;

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    private LocalDate dueDate;

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    private String dueType;

    public String getDueType() {
        return this.dueType;
    }

    public void setDueType(String dueType) {
        this.dueType = dueType;
    }

    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UpdateDueRequest() {
    }

    public UpdateDueRequest(String remarks, Double amount, LocalDate dueDate, String dueType, String status) {
        this.remarks = remarks;
        this.amount = amount;
        this.dueDate = dueDate;
        this.dueType = dueType;
        this.status = status;
    }
}
