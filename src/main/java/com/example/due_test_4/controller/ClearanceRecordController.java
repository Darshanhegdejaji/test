package com.example.due_test_4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.service.ClearanceRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Map;
import java.security.Principal;
import org.springframework.web.bind.annotation.*;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;

@RestController()
@RequestMapping(value = "/api/clearancerecords")
public class ClearanceRecordController {

    @Autowired()
    private ClearanceRecordService clearanceRecordService;

    @PostMapping()
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<ClearanceRecord> createClearanceRecord(@RequestBody ClearanceRecord entity) {
        return ResponseEntity.ok(clearanceRecordService.createClearanceRecord(entity));
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('STUDENT','DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<List<ClearanceRecord>> getAllClearanceRecords() {
        return ResponseEntity.ok(clearanceRecordService.getAllClearanceRecords());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<ClearanceRecord> getClearanceRecordById(@PathVariable String id) {
        return clearanceRecordService.getClearanceRecordById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<ClearanceRecord> updateClearanceRecord(@PathVariable String id, @RequestBody ClearanceRecord entity) {
        ClearanceRecord updated = clearanceRecordService.updateClearanceRecord(id, entity);
        if (updated != null)
            return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Void> deleteClearanceRecord(@PathVariable String id) {
        clearanceRecordService.deleteClearanceRecord(id);
        return ResponseEntity.noContent().build();
    }

    /*
 * Operation    : Get Clearance Record By Student
 * Comment      : Endpoint for the authenticated student to retrieve their own clearance record including total, cleared, and pending due counts and overall clearance status.
 */
    @GetMapping(value = "/api/clearance-records/me")
    @PreAuthorize("hasAnyRole('STUDENT','DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<ClearanceRecord> getClearanceRecordByStudent(Principal principal) {
        return ResponseEntity.ok(clearanceRecordService.getClearanceRecordByStudent(principal));
    }

    /*
 * Operation    : Update Clearance Record on Due Creation
 * Comment      : Endpoint called when a new due is created for a student to update the corresponding clearance record counts.
 */
    @PatchMapping(value = "/api/clearance-records/student/{studentId}/due-created")
    @PreAuthorize("hasAnyRole('DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<ClearanceRecord> updateClearanceRecordOnDueCreation(@PathVariable String studentId) {
        return ResponseEntity.ok(clearanceRecordService.updateClearanceRecordOnDueCreation(studentId));
    }

    /*
 * Operation    : Update Clearance Record on Due Cleared
 * Comment      : Endpoint to update the ClearanceRecord for a student after a due is marked as cleared. Delegates to ClearanceRecordService.
 */
    @PatchMapping(value = "/api/clearance-records/students/{studentId}/due-cleared")
    @PreAuthorize("hasAnyRole('DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<ClearanceRecord> updateClearanceRecordOnDueCleared(@PathVariable String studentId) {
        return ResponseEntity.ok(clearanceRecordService.updateClearanceRecordOnDueCleared(studentId));
    }

    /*
 * Operation    : Get Department Clearance Summary
 * Comment      : Endpoint for Department Admin to retrieve aggregated clearance summary statistics for their department.
 */
    @GetMapping(value = "/api/clearance-records/department/summary")
    @PreAuthorize("hasRole('DEPARTMENT ADMIN')")
    public ResponseEntity<Map<String, Object>> getDepartmentClearanceSummary(Principal principal) {
        return ResponseEntity.ok(clearanceRecordService.getDepartmentClearanceSummary(principal));
    }

    /*
 * Operation    : Get Student Clearance Record
 * Comment      : Endpoint to retrieve the overall clearance status and progress of a student across all departments.
 */
    @GetMapping(value = "/api/students/{studentId}/clearance-record")
    @PreAuthorize("hasAnyRole('SUPER ADMIN','STUDENT')")
    public ResponseEntity<ClearanceRecord> getStudentClearanceRecord(@PathVariable String studentId, Principal principal) {
        return ResponseEntity.ok(clearanceRecordService.getStudentClearanceRecord(studentId, principal));
    }

    /*
 * Operation    : Get Institution-Wide Clearance Statistics
 * Comment      : Exposes GET endpoint for Super Admin to retrieve aggregated institution-wide clearance statistics.
 */
    @GetMapping(value = "/api/clearance/statistics/institution")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Map<String, Long>> getInstitutionWideClearanceStatistics() {
        return ResponseEntity.ok(clearanceRecordService.getInstitutionWideClearanceStatistics());
    }

    /*
 * Operation    : Get Student Clearance Summary
 * Comment      : Endpoint for Super Admin to retrieve a student's clearance record and dues summary by student ID.
 */
    @GetMapping(value = "/api/admin/students/{studentId}/clearance-summary")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Map<String, Object>> getStudentClearanceSummary(@PathVariable String studentId) {
        return ResponseEntity.ok(clearanceRecordService.getStudentClearanceSummary(studentId));
    }

    /*
 * Operation    : Validate All Dues Cleared
 * Comment      : Endpoint for Super Admin to validate that all dues are cleared for a student before approving final no-dues clearance.
 */
    @GetMapping(value = "/api/clearance/validate-dues/{studentId}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Map<String, Object>> validateAllDuesCleared(@PathVariable String studentId) {
        return ResponseEntity.ok(clearanceRecordService.validateAllDuesCleared(studentId));
    }

    /*
 * Operation    : Approve Final Clearance
 * Comment      : Endpoint for Super Admin to approve final clearance for a specific student by studentId.
 */
    @PatchMapping(value = "/api/clearance/students/{studentId}/approve")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<ClearanceRecord> approveFinalClearance(@PathVariable String studentId, Principal principal) {
        return ResponseEntity.ok(clearanceRecordService.approveFinalClearance(studentId, principal));
    }

    /*
 * Operation    : Get Clearance Progress Analytics
 * Comment      : Endpoint for Super Admin to retrieve institution-wide clearance progress analytics.
 */
    @GetMapping(value = "/api/analytics/clearance-progress")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Map<String, Object>> getClearanceProgressAnalytics(Principal principal) {
        return ResponseEntity.ok(clearanceRecordService.getClearanceProgressAnalytics(principal));
    }
}
