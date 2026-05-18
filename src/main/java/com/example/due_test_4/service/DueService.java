package com.example.due_test_4.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.repository.DueRepository;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import com.example.due_test_4.repository.DepartmentAdminRepository;
import com.example.due_test_4.repository.StudentRepository;
import com.example.due_test_4.repository.DepartmentRepository;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;
import java.time.*;

@Service()
public class DueService {

    @Autowired()
    private DueRepository dueRepository;

    public Due createDue(Due entity) {
        return dueRepository.save(entity);
    }

    public List<Due> getAllDues() {
        return dueRepository.findAll();
    }

    public Due updateDue(String id, Due entity) {
        if (dueRepository.existsById(id)) {
            entity.setId(id);
            return dueRepository.save(entity);
        }
        return null;
    }

    public void deleteDue(String id) {
        dueRepository.deleteById(id);
    }

    public DueService(DueRepository dueRepository) {
        this.dueRepository = dueRepository;
    }

    private KeycloakAuthService keycloakAuthService;

    /*
 * Operation    : Get Dues By Student
 * Comment      : Extracts the authenticated student's ID via KeycloakAuthService and retrieves all associated due records.
 */
    public List<Due> getDuesByStudent(Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        List<Due> dues = dueRepository.findByStudentId(userId);
        return dues;
    }

    /*
 * Operation    : Get Dues Grouped By Department
 * Comment      : Retrieves all dues for the authenticated student and groups them by department (represented via dueType as the department grouping key). Returns a map of department name to list of dues.
 */
    public Map<String, List<Due>> getDuesGroupedByDepartment(Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        List<Due> dues = dueRepository.findByStudentId(userId);
        Map<String, List<Due>> groupedByDepartment = new java.util.LinkedHashMap<>();
        for (Due due : dues) {
            String dueType = due.getDueType();
            groupedByDepartment.computeIfAbsent(dueType, k -> new java.util.ArrayList<>()).add(due);
        }
        return groupedByDepartment;
    }

    /*
 * Operation    : Get Student Dues By Department
 * Comment      : Retrieves all due records for the authenticated student and groups them by department name.
 */
    public Map<String, List<Due>> getStudentDuesByDepartment(Principal principal) {
        String studentId = keycloakAuthService.getUserId(principal);
        List<Due> dues = dueRepository.findByStudentId(studentId);
        Map<String, List<Due>> duesByDepartment = new java.util.LinkedHashMap<>();
        for (Due due : dues) {
            String departmentKey = due.getDepartment() != null ? due.getDepartment().getName() : "Unknown";
            duesByDepartment.computeIfAbsent(departmentKey, k -> new java.util.ArrayList<>()).add(due);
        }
        return duesByDepartment;
    }

    /*
 * Operation    : Get Pending Dues For Student
 * Comment      : Extracts the authenticated student's ID via KeycloakAuthService and retrieves all dues with PENDING status for that student.
 */
    public List<Due> getPendingDuesForStudent(Principal principal) {
        String studentId = keycloakAuthService.getUserId(principal);
        List<Due> pendingDues = dueRepository.findByStudentIdAndStatus(studentId, "PENDING");
        return pendingDues;
    }

    private DepartmentAdminRepository departmentAdminRepository;

    private StudentRepository studentRepository;

    private DepartmentRepository departmentRepository;

    /*
 * Operation    : Create Due Record
 * Comment      : Authenticated Department Admin creates a new due record for a selected student. Validates admin and student existence, resolves the associated department, and persists the due record.
 */
    public Due createDueRecord(CreateDueRecordRequest request, Principal principal) {
        String adminId = keycloakAuthService.getUserId(principal);
        DepartmentAdmin departmentAdmin = departmentAdminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Department Admin not found"));
        if (!departmentAdmin.getIsActive()) {
            throw new RuntimeException("Department Admin is not active");
        }
        Student student = studentRepository.findById(request.getStudentId()).orElseThrow(() -> new RuntimeException("Student not found"));
        Department department = departmentRepository.findByAdminId(adminId).orElseThrow(() -> new RuntimeException("Department not found for this admin"));
        if (!department.getIsActive()) {
            throw new RuntimeException("Department is not active");
        }
        Due due = new Due();
        due.setDueType(request.getDueType());
        due.setAmount(request.getAmount());
        due.setRemarks(request.getRemarks());
        due.setStatus(request.getStatus());
        due.setDueDate(request.getDueDate());
        due.setCreatedAt(LocalDateTime.now());
        due.setUpdatedAt(LocalDateTime.now());
        return dueRepository.save(due);
    }

    /*
 * Operation    : Update Due Information
 * Comment      : Updates the specified fields of an existing Due record and refreshes the updatedAt timestamp. Only non-null fields from the request are applied.
 */
    public Due updateDueInformation(String id, UpdateDueRequest request) {
        Due due = dueRepository.findById(id).orElseThrow(() -> new RuntimeException("Due record not found with id: " + id));
        if (request.getRemarks() != null) {
            due.setRemarks(request.getRemarks());
        }
        if (request.getAmount() != null) {
            due.setAmount(request.getAmount());
        }
        if (request.getDueDate() != null) {
            due.setDueDate(request.getDueDate());
        }
        if (request.getDueType() != null) {
            due.setDueType(request.getDueType());
        }
        if (request.getStatus() != null) {
            due.setStatus(request.getStatus());
        }
        due.setUpdatedAt(LocalDateTime.now());
        return dueRepository.save(due);
    }

    /*
 * Operation    : Get Due By ID
 * Comment      : Fetches a specific Due record by its ID. Throws RuntimeException if not found.
 */
    public Due getDueById(String id) {
        Due due = dueRepository.findById(id).orElseThrow(() -> new RuntimeException("Due not found with id: " + id));
        return due;
    }

    /*
 * Operation    : Mark Due as Cleared
 * Comment      : Fetches the Due record by ID, updates its status to CLEARED and sets the clearedDate, then persists the changes.
 */
    public Due markDueAsCleared(String dueId, MarkDueClearedRequest request, Principal principal) {
        String adminId = keycloakAuthService.getUserId(principal);
        Due due = dueRepository.findById(dueId).orElseThrow(() -> new RuntimeException("Due not found with id: " + dueId));
        due.setStatus("CLEARED");
        due.setClearedDate(request.getClearedDate());
        due.setUpdatedAt(java.time.LocalDateTime.now());
        return dueRepository.save(due);
    }

    /*
 * Operation    : Get Department Pending Dues
 * Comment      : Fetches all due records for the specified department with optional filtering by status, graduation year, and due type. Verifies the department exists before querying.
 */
    public List<Due> getDepartmentPendingDues(Principal principal, String departmentId, String status, Integer graduationYear, String dueType) {
        String userId = keycloakAuthService.getUserId(principal);
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found"));
        List<Due> dues = dueRepository.findDuesByDepartmentAndFilters(departmentId, status, graduationYear, dueType);
        return dues;
    }

    /*
 * Operation    : Get Student Dues Across Departments
 * Comment      : Validates that the student exists, then retrieves all due records for that student across all departments
 */
    public List<Due> getStudentDuesAcrossDepartments(String studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        List<Due> dues = dueRepository.findByStudentId(studentId);
        return dues;
    }

    /*
 * Operation    : Get Department-wise Due Analytics
 * Comment      : Aggregates pending and cleared due counts and amounts grouped by each active department. Accessible only by Super Admin.
 */
    public List<Map<String, Object>> getDepartmentWiseDueAnalytics(Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        List<Department> departments = departmentRepository.findByIsActiveTrue();
        List<Map<String, Object>> analytics = new java.util.ArrayList<>();
        for (Department department : departments) {
            Map<String, Object> record = new java.util.LinkedHashMap<>();
            record.put("departmentId", department.getId());
            record.put("departmentName", department.getName());
            Long pendingCount = dueRepository.countByDepartmentIdAndStatus(department.getId(), "PENDING");
            Long clearedCount = dueRepository.countByDepartmentIdAndStatus(department.getId(), "CLEARED");
            Double pendingAmount = dueRepository.sumAmountByDepartmentIdAndStatus(department.getId(), "PENDING");
            Double clearedAmount = dueRepository.sumAmountByDepartmentIdAndStatus(department.getId(), "CLEARED");
            record.put("pendingCount", pendingCount != null ? pendingCount : 0L);
            record.put("clearedCount", clearedCount != null ? clearedCount : 0L);
            record.put("pendingAmount", pendingAmount != null ? pendingAmount : 0.0);
            record.put("clearedAmount", clearedAmount != null ? clearedAmount : 0.0);
            analytics.add(record);
        }
        return analytics;
    }

    /*
 * Operation    : Get Pending Dues Summary
 * Comment      : Calculates and returns a summary of all pending dues filtered by department, graduation year, or due date. Only accessible by Super Admin.
 */
    public Map<String, Object> getPendingDuesSummary(Principal principal, String department, Integer graduationYear, LocalDate dueDate) {
        String userId = keycloakAuthService.getUserId(principal);
        List<Due> pendingDues;
        if (dueDate != null) {
            pendingDues = dueRepository.findByStatusAndDueDateBefore("PENDING", dueDate);
        } else {
            pendingDues = dueRepository.findPendingDuesSummaryByDepartmentAndGraduationYear(department, graduationYear, "PENDING");
        }
        long totalPendingCount = pendingDues.size();
        Double totalPendingAmount = pendingDues.stream().filter(d -> d.getAmount() != null).mapToDouble(Due::getAmount).sum();
        Map<String, Long> countByDueType = pendingDues.stream().collect(java.util.stream.Collectors.groupingBy(Due::getDueType, java.util.stream.Collectors.counting()));
        Map<String, Object> summary = new java.util.HashMap<>();
        summary.put("totalPendingCount", totalPendingCount);
        summary.put("totalPendingAmount", totalPendingAmount);
        summary.put("countByDueType", countByDueType);
        summary.put("filterDepartment", department);
        summary.put("filterGraduationYear", graduationYear);
        summary.put("filterDueDate", dueDate);
        return summary;
    }
}
