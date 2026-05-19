package com.example.due_test_4.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.service.SuperAdminService;
import com.example.due_test_4.entity.SuperAdmin;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController()
@RequestMapping(value = "/api/superadmins")
public class SuperAdminController {

    @Autowired()
    private SuperAdminService superAdminService;

    @PostMapping()
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<SuperAdmin> createSuperAdmin(@RequestBody SuperAdmin entity) {
        return ResponseEntity.ok(superAdminService.createSuperAdmin(entity));
    }

    @GetMapping()
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<List<SuperAdmin>> getAllSuperAdmins() {
        return ResponseEntity.ok(superAdminService.getAllSuperAdmins());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<SuperAdmin> getSuperAdminById(@PathVariable String id) {
        return superAdminService.getSuperAdminById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<SuperAdmin> updateSuperAdmin(@PathVariable String id, @RequestBody SuperAdmin entity) {
        SuperAdmin updated = superAdminService.updateSuperAdmin(id, entity);
        if (updated != null)
            return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Void> deleteSuperAdmin(@PathVariable String id) {
        superAdminService.deleteSuperAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
