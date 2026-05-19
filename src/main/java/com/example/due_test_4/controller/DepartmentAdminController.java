package com.example.due_test_4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.service.DepartmentAdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Map;
import java.security.Principal;
import org.springframework.web.bind.annotation.*;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;

@RestController()
@RequestMapping(value = "/api/departmentadmins")
public class DepartmentAdminController {

    @Autowired()
    private DepartmentAdminService departmentAdminService;

    @GetMapping()
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<List<DepartmentAdmin>> getAllDepartmentAdmins() {
        return ResponseEntity.ok(departmentAdminService.getAllDepartmentAdmins());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<DepartmentAdmin> getDepartmentAdminById(@PathVariable String id) {
        return departmentAdminService.getDepartmentAdminById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Void> deleteDepartmentAdmin(@PathVariable String id) {
        departmentAdminService.deleteDepartmentAdmin(id);
        return ResponseEntity.noContent().build();
    }

    /*
 * Operation    : Create Department Admin
 * Comment      : Endpoint for Super Admin to create a new department admin account.
 */
    @PostMapping(value = "/api/department-admins")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<DepartmentAdmin> createDepartmentAdmin(@RequestBody CreateDepartmentAdminRequest request, Principal principal) {
        return ResponseEntity.ok(departmentAdminService.createDepartmentAdmin(request, principal));
    }

    /*
 * Operation    : Update Department Admin
 * Comment      : Endpoint for Super Admin to update an existing department admin account's profile or active status.
 */
    @PutMapping(value = "/api/department-admins/{adminId}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<DepartmentAdmin> updateDepartmentAdmin(@PathVariable String adminId, @RequestBody UpdateDepartmentAdminRequest request, Principal principal) {
        return ResponseEntity.ok(departmentAdminService.updateDepartmentAdmin(adminId, request, principal));
    }

    /*
 * Operation    : Assign Department Admin to Department
 * Comment      : Endpoint for Super Admin to assign a department admin to a specific department
 */
    @PostMapping(value = "/api/department-admins/assign")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<DepartmentAdmin> assignDepartmentAdminToDepartment(@RequestBody AssignDepartmentAdminRequest request, Principal principal) {
        return ResponseEntity.ok(departmentAdminService.assignDepartmentAdminToDepartment(request, principal));
    }

    /*
 * Operation    : Get Department Admin
 * Comment      : Endpoint to retrieve a specific department admin by ID. Accessible by Super Admin only.
 */
    @GetMapping(value = "/api/department-admins/{id}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<DepartmentAdmin> getDepartmentAdmin(@PathVariable String id) {
        return ResponseEntity.ok(departmentAdminService.getDepartmentAdmin(id));
    }

    /*
 * Operation    : List Department Admins
 * Comment      : Retrieves all department admin accounts with optional query parameters for departmentId and isActive filtering.
 */
    @GetMapping(value = "/api/department-admins")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<List<DepartmentAdmin>> listDepartmentAdmins(String departmentId, Boolean isActive) {
        return ResponseEntity.ok(departmentAdminService.listDepartmentAdmins(departmentId, isActive));
    }
}
