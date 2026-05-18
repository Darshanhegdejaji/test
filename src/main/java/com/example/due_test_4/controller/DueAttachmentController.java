package com.example.due_test_4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.service.DueAttachmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Map;
import java.security.Principal;
import org.springframework.web.bind.annotation.*;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;

@RestController()
@RequestMapping(value = "/api/dueattachments")
public class DueAttachmentController {

    @Autowired()
    private DueAttachmentService dueAttachmentService;

    @PostMapping()
    @PreAuthorize("hasRole('DEPARTMENT ADMIN')")
    public ResponseEntity<DueAttachment> createDueAttachment(@RequestBody DueAttachment entity) {
        return ResponseEntity.ok(dueAttachmentService.createDueAttachment(entity));
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('STUDENT','DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<List<DueAttachment>> getAllDueAttachments() {
        return ResponseEntity.ok(dueAttachmentService.getAllDueAttachments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<DueAttachment> getDueAttachmentById(@PathVariable String id) {
        return dueAttachmentService.getDueAttachmentById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DEPARTMENT ADMIN')")
    public ResponseEntity<DueAttachment> updateDueAttachment(@PathVariable String id, @RequestBody DueAttachment entity) {
        DueAttachment updated = dueAttachmentService.updateDueAttachment(id, entity);
        if (updated != null)
            return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<Void> deleteDueAttachment(@PathVariable String id) {
        dueAttachmentService.deleteDueAttachment(id);
        return ResponseEntity.noContent().build();
    }

    /*
 * Operation    : Add Due Attachment
 * Comment      : Accepts a new attachment payload and delegates to the service to associate it with the specified due record.
 */
    @PostMapping(value = "/api/dues/{dueId}/attachments")
    @PreAuthorize("hasAnyRole('DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<DueAttachment> addDueAttachment(@PathVariable String dueId, @RequestBody AddDueAttachmentRequest request) {
        return ResponseEntity.ok(dueAttachmentService.addDueAttachment(dueId, request));
    }

    /*
 * Operation    : Remove Due Attachment
 * Comment      : Deletes a specific DueAttachment by its ID. Delegates entirely to DueAttachmentService.
 */
    @DeleteMapping(value = "/api/dues/attachments/{attachmentId}")
    @PreAuthorize("hasAnyRole('DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<Void> removeDueAttachment(@PathVariable String attachmentId) {
        return ResponseEntity.ok(dueAttachmentService.removeDueAttachment(attachmentId));
    }
}
