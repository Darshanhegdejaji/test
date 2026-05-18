package com.example.due_test_4.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.repository.NoDesSummaryRepository;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import com.example.due_test_4.repository.ClearanceRecordRepository;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;
import java.time.*;

@Service()
public class NoDesSummaryService {

    @Autowired()
    private NoDesSummaryRepository noDesSummaryRepository;

    public List<NoDesSummary> getAllNoDesSummarys() {
        return noDesSummaryRepository.findAll();
    }

    public Optional<NoDesSummary> getNoDesSummaryById(String id) {
        return noDesSummaryRepository.findById(id);
    }

    public NoDesSummary updateNoDesSummary(String id, NoDesSummary entity) {
        if (noDesSummaryRepository.existsById(id)) {
            entity.setId(id);
            return noDesSummaryRepository.save(entity);
        }
        return null;
    }

    public void deleteNoDesSummary(String id) {
        noDesSummaryRepository.deleteById(id);
    }

    private ClearanceRecordRepository clearanceRecordRepository;

    public NoDesSummaryService(ClearanceRecordRepository clearanceRecordRepository) {
        this.clearanceRecordRepository = clearanceRecordRepository;
    }

    private KeycloakAuthService keycloakAuthService;

    /*
 * Operation    : Create No-Dues Summary
 * Comment      : Validates that the student is fully cleared, then persists a new NoDesSummary record with the provided document URL and timestamps.
 */
    public NoDesSummary createNoDesSummary(Principal principal, CreateNoDesSummaryRequest request) {
        String userId = keycloakAuthService.getUserId(principal);
        Student student = studentRepository.findById(userId).orElseThrow(() -> new RuntimeException("Student not found"));
        if (!student.getIsCleared()) {
            throw new RuntimeException("Student has not been fully cleared. Cannot generate No-Dues Summary.");
        }
        ClearanceRecord clearanceRecord = clearanceRecordRepository.findById(userId).orElseThrow(() -> new RuntimeException("Clearance record not found for student"));
        if (!"CLEARED".equalsIgnoreCase(clearanceRecord.getOverallStatus())) {
            throw new RuntimeException("Clearance record status is not CLEARED. Cannot generate No-Dues Summary.");
        }
        NoDesSummary summary = new NoDesSummary();
        summary.setId(java.util.UUID.randomUUID().toString());
        summary.setDocumentUrl(request.getDocumentUrl());
        summary.setGeneratedAt(request.getGeneratedAt());
        summary.setExpiresAt(request.getExpiresAt());
        NoDesSummary saved = noDesSummaryRepository.save(summary);
        return saved;
    }

    /*
 * Operation    : Download No-Dues Summary
 * Comment      : Retrieves the NoDesSummary record for the authenticated student using their ID extracted from the JWT token.
 */
    public NoDesSummary downloadNoDesSummary(Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        NoDesSummary noDesSummary = noDesSummaryRepository.findByStudentId(userId).orElseThrow(() -> new RuntimeException("No-Dues Summary not found for student"));
        return noDesSummary;
    }

    /*
 * Operation    : Export Clearance Report
 * Comment      : Authenticates the super admin and retrieves all NoDesSummary clearance report documents ordered by generation date.
 */
    public List<NoDesSummary> exportClearanceReport(Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        SuperAdmin superAdmin = superAdminRepository.findById(userId).orElseThrow(() -> new RuntimeException("SuperAdmin not found"));
        List<NoDesSummary> summaries = noDesSummaryRepository.findAllByOrderByGeneratedAtDesc();
        return summaries;
    }
}
