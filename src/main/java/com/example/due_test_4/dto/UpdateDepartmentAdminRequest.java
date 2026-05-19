package com.example.due_test_4.dto;

import lombok.Builder;
import lombok.Data;

@Builder()
@Data()
public class UpdateDepartmentAdminRequest {

    private String firstName;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String lastName;

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String phoneNumber;

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private Boolean isActive;

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public UpdateDepartmentAdminRequest() {
    }

    public UpdateDepartmentAdminRequest(String firstName, String lastName, String phoneNumber, Boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
    }
}
