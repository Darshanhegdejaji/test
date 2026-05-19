package com.example.due_test_4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.example.due_test_4.entity.*;

@Repository()
public interface DepartmentAdminRepository extends JpaRepository<DepartmentAdmin, String> {

    // repo_method_id: repo_find_department_admin_by_email | Check if a department admin with the given email already exists.
    Optional<DepartmentAdmin> findByEmail(String email);

    // repo_method_id: repo_find_department_admin_department_mapping_by_admin_id | Check if a department already has an admin assigned
    @Query(value = "SELECT da FROM DepartmentAdmin da WHERE da.department.id = :departmentId", nativeQuery = false)
    Optional<DepartmentAdmin> findByDepartmentId(String departmentId);

    // repo_method_id: repo_find_department_admins_by_is_active | Find all department admins filtered by active status.
    List<DepartmentAdmin> findByIsActive(Boolean isActive);

    // repo_method_id: repo_find_department_admins_by_department_id_and_is_active | Find all department admins filtered by both department and active status.
    List<DepartmentAdmin> findByDepartmentIdAndIsActive(String departmentId, Boolean isActive);
}
