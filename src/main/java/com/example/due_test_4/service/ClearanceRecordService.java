package com.example.due_test_4.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.repository.ClearanceRecordRepository;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import com.example.due_test_4.repository.DepartmentAdminRepository;
import com.example.due_test_4.repository.StudentRepository;
import com.example.due_test_4.repository.DueRepository;
import com.example.due_test_4.repository.SuperAdminRepository;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;
import java.time.*;

@Service()
public class ClearanceRecordService {

    @Autowired()
    private ClearanceRecordRepository clearanceRecordRepository;

    public ClearanceRecord createClearanceRecord(ClearanceRecord entity) {
        return clearanceRecordRepository.save(entity);
    }

    public List<ClearanceRecord> getAllClearanceRecords() {
        return clearanceRecordRepository.findAll();
    }

    public Optional<ClearanceRecord> getClearanceRecordById(String id) {
        return clearanceRecordRepository.findById(id);
    }

    public ClearanceRecord updateClearanceRecord(String id, ClearanceRecord entity) {
        if (clearanceRecordRepository.existsById(id)) {
            entity.setId(id);
            return clearanceRecordRepository.save(entity);
        }
        return null;
    }

    public void deleteClearanceRecord(String id) {
        clearanceRecordRepository.deleteById(id);
    }

    public ClearanceRecordService(ClearanceRecordRepository clearanceRecordRepository) {
        this.clearanceRecordRepository = clearanceRecordRepository;
    }

    private KeycloakAuthService keycloakAuthService;

    /*
 * Operation    : Get Clearance Record By Student
 * Comment      : Retrieves the clearance record for the authenticated student using their principal identity.
 */
    public ClearanceRecord getClearanceRecordByStudent(Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        ClearanceRecord clearanceRecord = clearanceRecordRepository.findByStudentId(userId).orElseThrow(() -> new RuntimeException("Clearance record not found for student: " + userId));
        return clearanceRecord;
    }

    /*
 * Operation    : Update Clearance Record on Due Creation
 * Comment      : Increments totalDues and pendingDues on the student's clearance record when a new due is created.
 */
    public ClearanceRecord updateClearanceRecordOnDueCreation(String studentId) {
        ClearanceRecord clearanceRecord = clearanceRecordRepository.findByStudentId(studentId).orElseThrow(() -> new RuntimeException("Clearance record not found for student: " + studentId));
        clearanceRecord.setTotalDues(clearanceRecord.getTotalDues() + 1);
        clearanceRecord.setPendingDues(clearanceRecord.getPendingDues() + 1);
        clearanceRecord.setUpdatedAt(java.time.LocalDateTime.now());
        return clearanceRecordRepository.save(clearanceRecord);
    }

    /*
 * Operation    : Update Clearance Record on Due Cleared
 * Comment      : Increments clearedDues, decrements pendingDues, recalculates overallStatus, and saves the updated ClearanceRecord.
 */
    public ClearanceRecord updateClearanceRecordOnDueCleared(String studentId) {
        ClearanceRecord clearanceRecord = clearanceRecordRepository.findByStudentId(studentId).orElseThrow(() -> new RuntimeException("ClearanceRecord not found for student: " + studentId));
        int updatedClearedDues = clearanceRecord.getClearedDues() + 1;
        int updatedPendingDues = clearanceRecord.getPendingDues() - 1;
        clearanceRecord.setClearedDues(updatedClearedDues);
        clearanceRecord.setPendingDues(updatedPendingDues);
        String newStatus;
        if (updatedPendingDues <= 0) {
            newStatus = "CLEARED";
        } else if (updatedClearedDues > 0) {
            newStatus = "IN_PROGRESS";
        } else {
            newStatus = "PENDING";
        }
        clearanceRecord.setOverallStatus(newStatus);
        clearanceRecord.setUpdatedAt(java.time.LocalDateTime.now());
        return clearanceRecordRepository.save(clearanceRecord);
    }

    private DepartmentAdminRepository departmentAdminRepository;

    /*
 * Operation    : Get Department Clearance Summary
 * Comment      : Retrieves aggregated clearance statistics for the authenticated Department Admin, including total, cleared, and pending dues counts across all students.
 */
    public Map<String, Object> getDepartmentClearanceSummary(Principal principal) {
        String adminId = keycloakAuthService.getUserId(principal);
        DepartmentAdmin admin = departmentAdminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Department Admin not found"));
        Long totalStudents = clearanceRecordRepository.countAllRecords();
        Long totalDuesSum = clearanceRecordRepository.sumTotalDues();
        Long clearedDuesSum = clearanceRecordRepository.sumClearedDues();
        Long pendingDuesSum = clearanceRecordRepository.sumPendingDues();
        Long fullyCleared = clearanceRecordRepository.countByOverallStatus("CLEARED");
        Long pendingStudents = clearanceRecordRepository.countByOverallStatus("PENDING");
        Map<String, Object> summary = new java.util.LinkedHashMap<>();
        summary.put("adminId", admin.getId());
        summary.put("adminName", admin.getFirstName() + " " + admin.getLastName());
        summary.put("totalStudents", totalStudents);
        summary.put("totalDues", totalDuesSum);
        summary.put("clearedDues", clearedDuesSum);
        summary.put("pendingDues", pendingDuesSum);
        summary.put("fullyCleared", fullyCleared);
        summary.put("pendingStudents", pendingStudents);
        return summary;
    }

    private StudentRepository studentRepository;

    /*
 * Operation    : Get Student Clearance Record
 * Comment      : Retrieves the clearance record for a given student. Validates that the student exists before fetching the clearance record.
 */
    public ClearanceRecord getStudentClearanceRecord(String studentId, Principal principal) {
        String requesterId = keycloakAuthService.getUserId(principal);
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        ClearanceRecord clearanceRecord = clearanceRecordRepository.findByStudentId(studentId).orElseThrow(() -> new RuntimeException("Clearance record not found for student"));
        return clearanceRecord;
    }

    /*
 * Operation    : Get Institution-Wide Clearance Statistics
 * Comment      : Aggregates institution-wide clearance statistics including student counts and dues summaries from ClearanceRecord and Student data.
 */
    public Map<String, Long> getInstitutionWideClearanceStatistics() {
        Long totalStudents = studentRepository.countAllStudents();
        Long clearedStudents = studentRepository.countByIsCleared(true);
        Long pendingStudents = totalStudents - clearedStudents;
        Long totalDues = clearanceRecordRepository.sumTotalDues();
        Long clearedDues = clearanceRecordRepository.sumClearedDues();
        Long pendingDues = clearanceRecordRepository.sumPendingDues();
        Long fullyCleared = clearanceRecordRepository.countByOverallStatus("CLEARED");
        Long fullyPending = clearanceRecordRepository.countByOverallStatus("PENDING");
        Map<String, Long> statistics = new java.util.LinkedHashMap<>();
        statistics.put("totalStudents", totalStudents);
        statistics.put("clearedStudents", clearedStudents);
        statistics.put("pendingStudents", pendingStudents);
        statistics.put("totalDues", totalDues);
        statistics.put("clearedDues", clearedDues);
        statistics.put("pendingDues", pendingDues);
        statistics.put("fullyCleared", fullyCleared);
        statistics.put("fullyPending", fullyPending);
        return statistics;
    }

    private DueRepository dueRepository;

    /*
 * Operation    : Get Student Clearance Summary
 * Comment      : Retrieves the student's profile, clearance record, and all associated dues for Super Admin review.
 */
    public Map<String, Object> getStudentClearanceSummary(String studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        ClearanceRecord clearanceRecord = clearanceRecordRepository.findByStudentId(studentId).orElseThrow(() -> new RuntimeException("Clearance record not found for student"));
        List<Due> dues = dueRepository.findByStudentId(studentId);
        Map<String, Object> summary = new java.util.LinkedHashMap<>();
        Map<String, Object> studentInfo = new java.util.LinkedHashMap<>();
        studentInfo.put("id", student.getId());
        studentInfo.put("firstName", student.getFirstName());
        studentInfo.put("lastName", student.getLastName());
        studentInfo.put("email", student.getEmail());
        studentInfo.put("enrollmentNumber", student.getEnrollmentNumber());
        studentInfo.put("department", student.getDepartment());
        studentInfo.put("graduationYear", student.getGraduationYear());
        studentInfo.put("isCleared", student.getIsCleared());
        Map<String, Object> clearanceInfo = new java.util.LinkedHashMap<>();
        clearanceInfo.put("id", clearanceRecord.getId());
        clearanceInfo.put("overallStatus", clearanceRecord.getOverallStatus());
        clearanceInfo.put("totalDues", clearanceRecord.getTotalDues());
        clearanceInfo.put("clearedDues", clearanceRecord.getClearedDues());
        clearanceInfo.put("pendingDues", clearanceRecord.getPendingDues());
        clearanceInfo.put("approvedAt", clearanceRecord.getApprovedAt());
        clearanceInfo.put("createdAt", clearanceRecord.getCreatedAt());
        clearanceInfo.put("updatedAt", clearanceRecord.getUpdatedAt());
        summary.put("student", studentInfo);
        summary.put("clearanceRecord", clearanceInfo);
        summary.put("dues", dues);
        return summary;
    }

    /*
 * Operation    : Validate All Dues Cleared
 * Comment      : Validates that no pending dues remain for the student by checking Due records and ClearanceRecord pending count. Returns a summary map indicating eligibility for final approval.
 */
    public Map<String, Object> validateAllDuesCleared(String studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Long pendingDueCount = dueRepository.countByStudentIdAndStatus(studentId, "PENDING");
        ClearanceRecord clearanceRecord = clearanceRecordRepository.findByStudentId(studentId).orElseThrow(() -> new RuntimeException("ClearanceRecord not found for student"));
        boolean allDuesCleared = (pendingDueCount == 0) && (clearanceRecord.getPendingDues() == 0);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("studentId", studentId);
        result.put("enrollmentNumber", student.getEnrollmentNumber());
        result.put("pendingDueCount", pendingDueCount);
        result.put("clearanceRecordPendingDues", clearanceRecord.getPendingDues());
        result.put("clearanceRecordTotalDues", clearanceRecord.getTotalDues());
        result.put("clearanceRecordClearedDues", clearanceRecord.getClearedDues());
        result.put("overallStatus", clearanceRecord.getOverallStatus());
        result.put("allDuesCleared", allDuesCleared);
        result.put("eligibleForFinalApproval", allDuesCleared);
        return result;
    }

    private SuperAdminRepository superAdminRepository;

    /*
 * Operation    : Approve Final Clearance
 * Comment      : Super Admin approves final clearance for a student. Sets student isCleared to true and records approvedAt timestamp in ClearanceRecord.
 */
    public ClearanceRecord approveFinalClearance(String studentId, Principal principal) {
        String adminId = keycloakAuthService.getUserId(principal);
        SuperAdmin superAdmin = superAdminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Super Admin not found"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        ClearanceRecord clearanceRecord = clearanceRecordRepository.findByStudentId(studentId).orElseThrow(() -> new RuntimeException("Clearance record not found for student"));
        student.setIsCleared(true);
        student.setUpdatedAt(LocalDateTime.now());
        studentRepository.save(student);
        clearanceRecord.setApprovedAt(LocalDateTime.now());
        clearanceRecord.setOverallStatus("APPROVED");
        clearanceRecord.setUpdatedAt(LocalDateTime.now());
        return clearanceRecordRepository.save(clearanceRecord);
    }

    /*
 * Operation    : Get Clearance Progress Analytics
 * Comment      : Aggregates institution-wide clearance progress analytics including status distributions, dues summaries, and student clearance counts. Only accessible by Super Admin.
 */
    public Map<String, Object> getClearanceProgressAnalytics(Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        Long totalRecords = clearanceRecordRepository.countAllRecords();
        Long approvedCount = clearanceRecordRepository.countByOverallStatus("APPROVED");
        Long pendingCount = clearanceRecordRepository.countByOverallStatus("PENDING");
        Long rejectedCount = clearanceRecordRepository.countByOverallStatus("REJECTED");
        Long inProgressCount = clearanceRecordRepository.countByOverallStatus("IN_PROGRESS");
        Long totalDuesSum = clearanceRecordRepository.sumTotalDues();
        Long clearedDuesSum = clearanceRecordRepository.sumClearedDues();
        Long pendingDuesSum = clearanceRecordRepository.sumPendingDues();
        Long totalClearedStudents = studentRepository.countByIsCleared(true);
        Long totalNotClearedStudents = studentRepository.countByIsCleared(false);
        Map<String, Object> statusDistribution = new java.util.LinkedHashMap<>();
        statusDistribution.put("APPROVED", approvedCount);
        statusDistribution.put("PENDING", pendingCount);
        statusDistribution.put("REJECTED", rejectedCount);
        statusDistribution.put("IN_PROGRESS", inProgressCount);
        Map<String, Object> duesSummary = new java.util.LinkedHashMap<>();
        duesSummary.put("totalDues", totalDuesSum);
        duesSummary.put("clearedDues", clearedDuesSum);
        duesSummary.put("pendingDues", pendingDuesSum);
        Map<String, Object> studentSummary = new java.util.LinkedHashMap<>();
        studentSummary.put("totalCleared", totalClearedStudents);
        studentSummary.put("totalNotCleared", totalNotClearedStudents);
        Map<String, Object> analytics = new java.util.LinkedHashMap<>();
        analytics.put("totalClearanceRecords", totalRecords);
        analytics.put("statusDistribution", statusDistribution);
        analytics.put("duesSummary", duesSummary);
        analytics.put("studentClearanceSummary", studentSummary);
        return analytics;
    }
}
