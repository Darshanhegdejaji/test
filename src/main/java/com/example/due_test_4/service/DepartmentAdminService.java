package com.example.due_test_4.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.repository.DepartmentAdminRepository;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import com.example.due_test_4.repository.DepartmentRepository;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;
import java.time.*;

@Service()
public class DepartmentAdminService {

    @Autowired()
    private DepartmentAdminRepository departmentAdminRepository;

    public List<DepartmentAdmin> getAllDepartmentAdmins() {
        return departmentAdminRepository.findAll();
    }

    public Optional<DepartmentAdmin> getDepartmentAdminById(String id) {
        return departmentAdminRepository.findById(id);
    }

    public void deleteDepartmentAdmin(String id) {
        departmentAdminRepository.deleteById(id);
    }

    public DepartmentAdminService(DepartmentAdminRepository departmentAdminRepository) {
        this.departmentAdminRepository = departmentAdminRepository;
    }

    private KeycloakAuthService keycloakAuthService;

    /*
 * Operation    : Create Department Admin
 * Comment      : Super Admin creates a new department admin account with required profile details and activates it in the system.
 */
    public DepartmentAdmin createDepartmentAdmin(CreateDepartmentAdminRequest request, Principal principal) {
        String superAdminId = keycloakAuthService.getUserId(principal);
        SuperAdmin superAdmin = superAdminRepository.findById(superAdminId).orElseThrow(() -> new RuntimeException("Super Admin not found"));
        boolean emailExists = departmentAdminRepository.findByEmail(request.getEmail()).isPresent();
        if (emailExists) {
            throw new RuntimeException("A department admin with this email already exists");
        }
        DepartmentAdmin departmentAdmin = new DepartmentAdmin();
        departmentAdmin.setId(request.getId());
        departmentAdmin.setFirstName(request.getFirstName());
        departmentAdmin.setLastName(request.getLastName());
        departmentAdmin.setEmail(request.getEmail());
        departmentAdmin.setPhoneNumber(request.getPhoneNumber());
        departmentAdmin.setIsActive(true);
        departmentAdmin.setCreatedAt(LocalDateTime.now());
        departmentAdmin.setUpdatedAt(LocalDateTime.now());
        return departmentAdminRepository.save(departmentAdmin);
    }

    /*
 * Operation    : Update Department Admin
 * Comment      : Super Admin updates an existing department admin's profile information or active status. Only non-null fields from the request are applied.
 */
    public DepartmentAdmin updateDepartmentAdmin(String adminId, UpdateDepartmentAdminRequest request, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        SuperAdmin superAdmin = superAdminRepository.findById(userId).orElseThrow(() -> new RuntimeException("Super Admin not found"));
        DepartmentAdmin departmentAdmin = departmentAdminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Department Admin not found"));
        if (request.getFirstName() != null) {
            departmentAdmin.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            departmentAdmin.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            departmentAdmin.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getIsActive() != null) {
            departmentAdmin.setIsActive(request.getIsActive());
        }
        departmentAdmin.setUpdatedAt(LocalDateTime.now());
        return departmentAdminRepository.save(departmentAdmin);
    }

    private DepartmentRepository departmentRepository;

    /*
 * Operation    : Assign Department Admin to Department
 * Comment      : Super Admin assigns a department admin to a specific department. Validates both entities exist and are active, checks no other admin is already assigned to the department, then persists the assignment.
 */
    public DepartmentAdmin assignDepartmentAdminToDepartment(AssignDepartmentAdminRequest request, Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        DepartmentAdmin departmentAdmin = departmentAdminRepository.findById(request.getDepartmentAdminId()).orElseThrow(() -> new RuntimeException("DepartmentAdmin not found with id: " + request.getDepartmentAdminId()));
        Department department = departmentRepository.findById(request.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found with id: " + request.getDepartmentId()));
        if (!department.getIsActive()) {
            throw new RuntimeException("Cannot assign admin to an inactive department");
        }
        if (!departmentAdmin.getIsActive()) {
            throw new RuntimeException("Cannot assign an inactive department admin");
        }
        Optional<DepartmentAdmin> existingAdmin = departmentAdminRepository.findByDepartmentId(request.getDepartmentId());
        if (existingAdmin.isPresent() && !existingAdmin.get().getId().equals(request.getDepartmentAdminId())) {
            throw new RuntimeException("Department already has an admin assigned");
        }
        departmentAdmin.setDepartment(department);
        departmentAdmin.setUpdatedAt(LocalDateTime.now());
        return departmentAdminRepository.save(departmentAdmin);
    }

    /*
 * Operation    : Get Department Admin
 * Comment      : Retrieves a specific DepartmentAdmin by their String ID. Uses Spring Data JPA default findById.
 */
    public DepartmentAdmin getDepartmentAdmin(String id) {
        DepartmentAdmin departmentAdmin = departmentAdminRepository.findById(id).orElseThrow(() -> new RuntimeException("DepartmentAdmin not found with id: " + id));
        return departmentAdmin;
    }

    /*
 * Operation    : List Department Admins
 * Comment      : Returns a list of department admins optionally filtered by departmentId and/or isActive status.
 */
    public List<DepartmentAdmin> listDepartmentAdmins(String departmentId, Boolean isActive) {
        if (departmentId != null && isActive != null) {
            return departmentAdminRepository.findByDepartmentIdAndIsActive(departmentId, isActive);
        } else if (departmentId != null) {
            return departmentAdminRepository.findByDepartmentId(departmentId);
        } else if (isActive != null) {
            return departmentAdminRepository.findByIsActive(isActive);
        } else {
            return departmentAdminRepository.findAll();
        }
    }
}
