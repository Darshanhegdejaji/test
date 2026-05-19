package com.example.due_test_4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.service.NoDesSummaryService;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Map;
import java.security.Principal;
import org.springframework.web.bind.annotation.*;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;

@RestController()
@RequestMapping(value = "/api/nodessummarys")
public class NoDesSummaryController {

    @Autowired()
    private NoDesSummaryService noDesSummaryService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('STUDENT','SUPER ADMIN')")
    public ResponseEntity<List<NoDesSummary>> getAllNoDesSummarys() {
        return ResponseEntity.ok(noDesSummaryService.getAllNoDesSummarys());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','SUPER ADMIN')")
    public ResponseEntity<NoDesSummary> getNoDesSummaryById(@PathVariable String id) {
        return noDesSummaryService.getNoDesSummaryById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<NoDesSummary> updateNoDesSummary(@PathVariable String id, @RequestBody NoDesSummary entity) {
        NoDesSummary updated = noDesSummaryService.updateNoDesSummary(id, entity);
        if (updated != null)
            return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Void> deleteNoDesSummary(@PathVariable String id) {
        noDesSummaryService.deleteNoDesSummary(id);
        return ResponseEntity.noContent().build();
    }

    /*
 * Operation    : Create No-Dues Summary
 * Comment      : Accepts a POST request from an authenticated student to generate and persist a No-Dues Summary document record.
 */
    @PostMapping(value = "/api/no-dues-summary")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<NoDesSummary> createNoDesSummary(Principal principal, @RequestBody CreateNoDesSummaryRequest request) {
        return ResponseEntity.ok(noDesSummaryService.createNoDesSummary(principal, request));
    }

    /*
 * Operation    : Download No-Dues Summary
 * Comment      : Endpoint for authenticated students to retrieve their No-Dues Summary document URL for download.
 */
    @GetMapping(value = "/api/no-dues-summary/download")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<NoDesSummary> downloadNoDesSummary(Principal principal) {
        return ResponseEntity.ok(noDesSummaryService.downloadNoDesSummary(principal));
    }

    /*
 * Operation    : Export Clearance Report
 * Comment      : Endpoint for Super Admin to export all institutional clearance report documents as NoDesSummary records.
 */
    @GetMapping(value = "/api/reports/clearance/export")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<List<NoDesSummary>> exportClearanceReport(Principal principal) {
        return ResponseEntity.ok(noDesSummaryService.exportClearanceReport(principal));
    }
}
