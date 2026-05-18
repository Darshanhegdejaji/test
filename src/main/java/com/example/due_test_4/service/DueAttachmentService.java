package com.example.due_test_4.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.repository.DueAttachmentRepository;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import com.example.due_test_4.repository.DueRepository;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;
import java.time.*;

@Service()
public class DueAttachmentService {

    @Autowired()
    private DueAttachmentRepository dueAttachmentRepository;

    public DueAttachment createDueAttachment(DueAttachment entity) {
        return dueAttachmentRepository.save(entity);
    }

    public List<DueAttachment> getAllDueAttachments() {
        return dueAttachmentRepository.findAll();
    }

    public Optional<DueAttachment> getDueAttachmentById(String id) {
        return dueAttachmentRepository.findById(id);
    }

    public DueAttachment updateDueAttachment(String id, DueAttachment entity) {
        if (dueAttachmentRepository.existsById(id)) {
            entity.setId(id);
            return dueAttachmentRepository.save(entity);
        }
        return null;
    }

    public void deleteDueAttachment(String id) {
        dueAttachmentRepository.deleteById(id);
    }

    private DueRepository dueRepository;

    public DueAttachmentService(DueRepository dueRepository, DueAttachmentRepository dueAttachmentRepository) {
        this.dueRepository = dueRepository;
        this.dueAttachmentRepository = dueAttachmentRepository;
    }

    private KeycloakAuthService keycloakAuthService;

    /*
 * Operation    : Add Due Attachment
 * Comment      : Creates a new DueAttachment linked to the specified Due record and persists it.
 */
    public DueAttachment addDueAttachment(String dueId, AddDueAttachmentRequest request) {
        Due due = dueRepository.findById(dueId).orElseThrow(() -> new RuntimeException("Due not found with id: " + dueId));
        DueAttachment attachment = new DueAttachment();
        attachment.setFileName(request.getFileName());
        attachment.setFileUrl(request.getFileUrl());
        attachment.setFileType(request.getFileType());
        attachment.setUploadedAt(LocalDateTime.now());
        attachment.setDue(due);
        return dueAttachmentRepository.save(attachment);
    }

    /*
 * Operation    : Remove Due Attachment
 * Comment      : Finds the DueAttachment by ID and deletes it. Throws RuntimeException if not found.
 */
    public Void removeDueAttachment(String attachmentId) {
        DueAttachment attachment = dueAttachmentRepository.findById(attachmentId).orElseThrow(() -> new RuntimeException("DueAttachment not found with id: " + attachmentId));
        dueAttachmentRepository.deleteById(attachmentId);
    }
}
