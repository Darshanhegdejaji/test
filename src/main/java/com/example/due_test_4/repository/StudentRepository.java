package com.example.due_test_4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.example.due_test_4.entity.*;

@Repository()
public interface StudentRepository extends JpaRepository<Student, String> {

    // repo_method_id: repo_search_student_by_name_or_enrollment | Search students by partial match on firstName, lastName, or enrollmentNumber (case-insensitive).
    List<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEnrollmentNumberContainingIgnoreCase(String firstName, String lastName, String enrollmentNumber);

    // repo_method_id: repo_search_students_by_keyword | Search students by name, email, enrollment number, or department using a keyword.
    @Query(value = "SELECT s FROM Student s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.enrollmentNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.department) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = false)
    List<Student> searchStudents(String keyword);

    // repo_method_id: repo_count_all_students | Counts total number of students institution-wide.
    @Query(value = "SELECT COUNT(s) FROM Student s", nativeQuery = false)
    Long countAllStudents();

    // repo_method_id: repo_count_cleared_students | Counts students by their isCleared status.
    Long countByIsCleared(Boolean isCleared);

    // repo_method_id: repo_find_all_students | Fetches all students ordered by creation date descending for report generation.
    List<Student> findAllByOrderByCreatedAtDesc();

    // repo_method_id: repo_student_find_by_department | Fetches all students belonging to a specific department.
    List<Student> findByDepartment(String department);

    // repo_method_id: repo_student_find_by_graduation_year | Fetches all students with a specific graduation year.
    List<Student> findByGraduationYear(Integer graduationYear);
}
