package com.example.due_test_4.dto;

import lombok.Builder;
import lombok.Data;

@Builder()
@Data()
public class AssignDepartmentAdminRequest {

    private String departmentAdminId;

    public String getDepartmentAdminId() {
        return this.departmentAdminId;
    }

    public void setDepartmentAdminId(String departmentAdminId) {
        this.departmentAdminId = departmentAdminId;
    }

    private String departmentId;

    public String getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public AssignDepartmentAdminRequest() {
    }

    public AssignDepartmentAdminRequest(String departmentAdminId, String departmentId) {
        this.departmentAdminId = departmentAdminId;
        this.departmentId = departmentId;
    }
}
