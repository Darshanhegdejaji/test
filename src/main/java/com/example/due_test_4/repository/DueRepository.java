package com.example.due_test_4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.example.due_test_4.entity.*;

@Repository()
public interface DueRepository extends JpaRepository<Due, String> {

    // repo_method_id: repo_find_dues_by_student_id | Retrieves all Due records associated with the given student ID.
    List<Due> findByStudentId(String studentId);

    // repo_method_id: repo_find_dues_by_student_and_status | Fetches all Due records associated with a specific student ID and a given status value.
    List<Due> findByStudentIdAndStatus(String studentId, String status);

    // repo_method_id: repo_find_dues_by_department_and_filters | Retrieves dues for a given department with optional filters on status, graduation year, and due type.
    @Query(value = "SELECT d FROM Due d JOIN Student s ON d.student.id = s.id WHERE s.department = :departmentId AND (:status IS NULL OR d.status = :status) AND (:graduationYear IS NULL OR s.graduationYear = :graduationYear) AND (:dueType IS NULL OR d.dueType = :dueType)", nativeQuery = false)
    List<Due> findDuesByDepartmentAndFilters(String departmentId, String status, Integer graduationYear, String dueType);

    // repo_method_id: repo_count_dues_by_department_and_status | Counts dues for a given department filtered by status (e.g., PENDING or CLEARED).
    Long countByDepartmentIdAndStatus(String departmentId, String status);

    // repo_method_id: repo_count_pending_dues_by_student | Count Due records for a student with a specific status (e.g., PENDING).
    @Query(value = "SELECT COUNT(d) FROM Due d WHERE d.student.id = :studentId AND d.status = :status", nativeQuery = false)
    Long countByStudentIdAndStatus(String studentId, String status);

    // repo_method_id: repo_due_sum_amount_by_department_and_status | Aggregates total due amount for a given department and status using JPQL.
    @Query(value = "SELECT COALESCE(SUM(d.amount), 0.0) FROM Due d WHERE d.department.id = :departmentId AND d.status = :status", nativeQuery = false)
    Double sumAmountByDepartmentIdAndStatus(String departmentId, String status);

    // repo_method_id: repo_due_find_by_status | Fetches all due records with a given status (e.g., PENDING).
    List<Due> findByStatus(String status);

    // repo_method_id: repo_due_find_by_status_and_due_date_before | Fetches all pending dues with a due date before the specified date.
    List<Due> findByStatusAndDueDateBefore(String status, LocalDate dueDate);

    // repo_method_id: repo_due_find_by_status_and_due_date_before_native | Fetches pending dues filtered by department and graduation year using a native query joining Due and Student tables.
    @Query(value = "SELECT d.* FROM due d JOIN student s ON d.student_id = s.id WHERE d.status = :status AND (:department IS NULL OR s.department = :department) AND (:graduationYear IS NULL OR s.graduation_year = :graduationYear)", nativeQuery = true)
    List<Due> findPendingDuesSummaryByDepartmentAndGraduationYear(String department, Integer graduationYear, String status);
}
