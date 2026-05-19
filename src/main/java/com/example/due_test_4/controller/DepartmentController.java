package com.example.due_test_4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.service.DepartmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Map;
import java.security.Principal;
import org.springframework.web.bind.annotation.*;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;

@RestController()
@RequestMapping(value = "/api/departments")
public class DepartmentController {

    @Autowired()
    private DepartmentService departmentService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<Department> getDepartmentById(@PathVariable String id) {
        return departmentService.getDepartmentById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable String id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    /*
 * Operation    : Create Department
 * Comment      : Endpoint for Super Admin to create a new department.
 */
    @PostMapping(value = "/api/departments")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Department> createDepartment(@RequestBody CreateDepartmentRequest request, Principal principal) {
        return ResponseEntity.ok(departmentService.createDepartment(request, principal));
    }

    /*
 * Operation    : Update Department
 * Comment      : Endpoint for Super Admin to update an existing department's configuration details.
 */
    @PutMapping(value = "/api/departments/{departmentId}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Department> updateDepartment(@PathVariable String departmentId, @RequestBody UpdateDepartmentRequest request, Principal principal) {
        return ResponseEntity.ok(departmentService.updateDepartment(departmentId, request, principal));
    }

    /*
 * Operation    : Activate Department
 * Comment      : Endpoint to activate a department by its ID. Restricted to Super Admin role.
 */
    @PatchMapping(value = "/api/departments/{id}/activate")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Department> activateDepartment(@PathVariable String id) {
        return ResponseEntity.ok(departmentService.activateDepartment(id));
    }

    /*
 * Operation    : Get Department-Wise Clearance Summary
 * Comment      : Exposes a GET endpoint for Super Admin to retrieve department-wise clearance summary including pending and cleared due counts per department.
 */
    @GetMapping(value = "/api/admin/clearance/department-summary")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getDepartmentWiseClearanceSummary() {
        return ResponseEntity.ok(departmentService.getDepartmentWiseClearanceSummary());
    }
}
