package com.example.due_test_4.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.entity.SuperAdmin;
import com.example.due_test_4.repository.SuperAdminRepository;

@Service()
public class SuperAdminService {

    @Autowired()
    private SuperAdminRepository superAdminRepository;

    public SuperAdmin createSuperAdmin(SuperAdmin entity) {
        return superAdminRepository.save(entity);
    }

    public List<SuperAdmin> getAllSuperAdmins() {
        return superAdminRepository.findAll();
    }

    public Optional<SuperAdmin> getSuperAdminById(String id) {
        return superAdminRepository.findById(id);
    }

    public SuperAdmin updateSuperAdmin(String id, SuperAdmin entity) {
        if (superAdminRepository.existsById(id)) {
            entity.setId(id);
            return superAdminRepository.save(entity);
        }
        return null;
    }

    public void deleteSuperAdmin(String id) {
        superAdminRepository.deleteById(id);
    }
}
