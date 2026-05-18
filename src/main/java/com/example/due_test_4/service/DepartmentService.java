package com.example.due_test_4.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.repository.DepartmentRepository;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;
import java.time.*;
import com.example.due_test_4.repository.DueRepository;

@Service()
public class DepartmentService {

    @Autowired()
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(String id) {
        return departmentRepository.findById(id);
    }

    public void deleteDepartment(String id) {
        departmentRepository.deleteById(id);
    }

    private KeycloakAuthService keycloakAuthService;

    public DepartmentService(KeycloakAuthService keycloakAuthService) {
        this.keycloakAuthService = keycloakAuthService;
    }

    /*
 * Operation    : Create Department
 * Comment      : Creates a new department with the provided name and description, sets it as active, and persists it.
 */
    public Department createDepartment(CreateDepartmentRequest request, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        Department department = new Department();
        department.setId(java.util.UUID.randomUUID().toString());
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setIsActive(true);
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        return departmentRepository.save(department);
    }

    /*
 * Operation    : Update Department
 * Comment      : Super Admin updates an existing department's name and/or description. Validates name uniqueness before saving.
 */
    public Department updateDepartment(String departmentId, UpdateDepartmentRequest request, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found"));
        if (request.getName() != null && !request.getName().equals(department.getName())) {
            Optional<Department> existingByName = departmentRepository.findByName(request.getName());
            if (existingByName.isPresent()) {
                throw new RuntimeException("Department with this name already exists");
            }
            department.setName(request.getName());
        }
        if (request.getDescription() != null) {
            department.setDescription(request.getDescription());
        }
        department.setUpdatedAt(LocalDateTime.now());
        return departmentRepository.save(department);
    }

    /*
 * Operation    : Activate Department
 * Comment      : Activates a department by setting its isActive flag to true and updating the updatedAt timestamp.
 */
    public Department activateDepartment(String id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
        department.setIsActive(true);
        department.setUpdatedAt(LocalDateTime.now());
        return departmentRepository.save(department);
    }

    private DueRepository dueRepository;

    /*
 * Operation    : Get Department-Wise Clearance Summary
 * Comment      : Iterates over all active departments and aggregates pending and cleared due counts for each department to produce a department-wise clearance summary.
 */
    public List<Map<String, Object>> getDepartmentWiseClearanceSummary() {
        List<Department> departments = departmentRepository.findByIsActiveTrue();
        List<Map<String, Object>> summary = new java.util.ArrayList<>();
        for (Department dept : departments) {
            Long pendingCount = dueRepository.countByDepartmentIdAndStatus(dept.getId(), "PENDING");
            Long clearedCount = dueRepository.countByDepartmentIdAndStatus(dept.getId(), "CLEARED");
            Long totalCount = pendingCount + clearedCount;
            Map<String, Object> entry = new java.util.LinkedHashMap<>();
            entry.put("departmentId", dept.getId());
            entry.put("departmentName", dept.getName());
            entry.put("totalDues", totalCount);
            entry.put("clearedDues", clearedCount);
            entry.put("pendingDues", pendingCount);
            summary.add(entry);
        }
        return summary;
    }
}
