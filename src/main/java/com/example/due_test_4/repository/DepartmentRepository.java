package com.example.due_test_4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.example.due_test_4.entity.*;

@Repository()
public interface DepartmentRepository extends JpaRepository<Department, String> {

    // repo_method_id: repo_find_department_by_id | Finds all departments matching the given list of IDs.
    List<Department> findByIdIn(List<String> ids);

    // repo_method_id: repo_find_department_by_admin_id | Find the department associated with the given department admin ID.
    @Query(value = "SELECT d FROM Department d JOIN d.departmentAdmins da WHERE da.id = :adminId", nativeQuery = false)
    Optional<Department> findByAdminId(String adminId);

    // repo_method_id: repo_find_department_by_name | Find department by name to validate uniqueness before update.
    Optional<Department> findByName(String name);

    // repo_method_id: repo_find_all_departments | Retrieves all active departments for summary generation.
    List<Department> findByIsActiveTrue();
}
