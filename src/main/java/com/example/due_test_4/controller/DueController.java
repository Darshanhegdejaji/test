package com.example.due_test_4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.service.DueService;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Map;
import java.security.Principal;
import org.springframework.web.bind.annotation.*;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;

@RestController()
@RequestMapping(value = "/api/dues")
public class DueController {

    @Autowired()
    private DueService dueService;

    @PostMapping()
    @PreAuthorize("hasRole('DEPARTMENT ADMIN')")
    public ResponseEntity<Due> createDue(@RequestBody Due entity) {
        return ResponseEntity.ok(dueService.createDue(entity));
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('STUDENT','DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<List<Due>> getAllDues() {
        return ResponseEntity.ok(dueService.getAllDues());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DEPARTMENT ADMIN')")
    public ResponseEntity<Due> updateDue(@PathVariable String id, @RequestBody Due entity) {
        Due updated = dueService.updateDue(id, entity);
        if (updated != null)
            return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Void> deleteDue(@PathVariable String id) {
        dueService.deleteDue(id);
        return ResponseEntity.noContent().build();
    }

    /*
 * Operation    : Get Dues By Student
 * Comment      : Returns all due records for the authenticated student.
 */
    @GetMapping(value = "/api/dues/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Due>> getDuesByStudent(Principal principal) {
        return ResponseEntity.ok(dueService.getDuesByStudent(principal));
    }

    /*
 * Operation    : Get Dues Grouped By Department
 * Comment      : Returns all dues for the authenticated student grouped by department. Delegates to DueService.
 */
    @GetMapping(value = "/api/students/me/dues/grouped-by-department")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, List<Due>>> getDuesGroupedByDepartment(Principal principal) {
        return ResponseEntity.ok(dueService.getDuesGroupedByDepartment(principal));
    }

    /*
 * Operation    : Get Student Dues By Department
 * Comment      : Endpoint for authenticated students to retrieve their dues grouped by department.
 */
    @GetMapping(value = "/api/students/me/dues/by-department")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, List<Due>>> getStudentDuesByDepartment(Principal principal) {
        return ResponseEntity.ok(dueService.getStudentDuesByDepartment(principal));
    }

    /*
 * Operation    : Get Pending Dues For Student
 * Comment      : Endpoint for the authenticated student to retrieve all their pending dues required for graduation clearance.
 */
    @GetMapping(value = "/api/students/me/dues/pending")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Due>> getPendingDuesForStudent(Principal principal) {
        return ResponseEntity.ok(dueService.getPendingDuesForStudent(principal));
    }

    /*
 * Operation    : Create Due Record
 * Comment      : Endpoint for Department Admin to create a new due record for a student. Delegates to DueService and returns the created due record.
 */
    @PostMapping(value = "/api/dues")
    @PreAuthorize("hasRole('DEPARTMENT ADMIN')")
    public ResponseEntity<Due> createDueRecord(@RequestBody CreateDueRecordRequest request, Principal principal) {
        return ResponseEntity.ok(dueService.createDueRecord(request, principal));
    }

    /*
 * Operation    : Update Due Information
 * Comment      : Accepts a PATCH request with updatable due fields and delegates to DueService to apply changes and refresh the updatedAt timestamp.
 */
    @PatchMapping(value = "/api/dues/{id}")
    @PreAuthorize("hasAnyRole('DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<Due> updateDueInformation(@PathVariable String id, @RequestBody UpdateDueRequest request) {
        return ResponseEntity.ok(dueService.updateDueInformation(id, request));
    }

    /*
 * Operation    : Get Due By ID
 * Comment      : Endpoint for Department Admin to retrieve a specific due record by its ID.
 */
    @GetMapping(value = "/api/dues/{id}")
    @PreAuthorize("hasRole('DEPARTMENT ADMIN')")
    public ResponseEntity<Due> getDueById(@PathVariable String id) {
        return ResponseEntity.ok(dueService.getDueById(id));
    }

    /*
 * Operation    : Mark Due as Cleared
 * Comment      : Endpoint for Department Admin to mark a specific due record as cleared.
 */
    @PatchMapping(value = "/api/dues/{dueId}/clear")
    @PreAuthorize("hasRole('DEPARTMENT ADMIN')")
    public ResponseEntity<Due> markDueAsCleared(@PathVariable String dueId, @RequestBody MarkDueClearedRequest request, Principal principal) {
        return ResponseEntity.ok(dueService.markDueAsCleared(dueId, request, principal));
    }

    /*
 * Operation    : Get Department Pending Dues
 * Comment      : Endpoint to retrieve all pending dues for a specific department with optional query parameters: status, graduationYear, dueType.
 */
    @GetMapping(value = "/api/departments/{departmentId}/dues")
    @PreAuthorize("hasAnyRole('DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<List<Due>> getDepartmentPendingDues(Principal principal, @PathVariable String departmentId, String status, Integer graduationYear, String dueType) {
        return ResponseEntity.ok(dueService.getDepartmentPendingDues(principal, departmentId, status, graduationYear, dueType));
    }

    /*
 * Operation    : Get Student Dues Across Departments
 * Comment      : Returns all due records for a specific student across all departments; accessible by Super Admin, Department Admin, and the Student themselves
 */
    @GetMapping(value = "/api/students/{studentId}/dues")
    @PreAuthorize("hasAnyRole('SUPER ADMIN','DEPARTMENT ADMIN','STUDENT')")
    public ResponseEntity<List<Due>> getStudentDuesAcrossDepartments(@PathVariable String studentId) {
        return ResponseEntity.ok(dueService.getStudentDuesAcrossDepartments(studentId));
    }

    /*
 * Operation    : Get Department-wise Due Analytics
 * Comment      : Exposes department-wise due analytics endpoint for Super Admin. Delegates to DueService and wraps result in ResponseEntity.
 */
    @GetMapping(value = "/api/analytics/department-wise-dues")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getDepartmentWiseDueAnalytics(Principal principal) {
        return ResponseEntity.ok(dueService.getDepartmentWiseDueAnalytics(principal));
    }

    /*
 * Operation    : Get Pending Dues Summary
 * Comment      : Endpoint for Super Admin to retrieve a summary of all pending dues, optionally filtered by department, graduation year, or due date as query parameters.
 */
    @GetMapping(value = "/api/dues/pending/summary")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Map<String, Object>> getPendingDuesSummary(Principal principal, String department, Integer graduationYear, LocalDate dueDate) {
        return ResponseEntity.ok(dueService.getPendingDuesSummary(principal, department, graduationYear, dueDate));
    }
}
