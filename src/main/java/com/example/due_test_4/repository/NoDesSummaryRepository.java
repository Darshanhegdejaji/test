package com.example.due_test_4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.example.due_test_4.entity.*;

@Repository()
public interface NoDesSummaryRepository extends JpaRepository<NoDesSummary, String> {

    // repo_method_id: repo_find_no_des_summary_by_student_id | Find the NoDesSummary record associated with the given student ID.
    @Query(value = "SELECT n FROM NoDesSummary n WHERE n.student.id = :studentId", nativeQuery = false)
    Optional<NoDesSummary> findByStudentId(String studentId);

    // repo_method_id: repo_find_all_no_des_summaries | Retrieves all NoDesSummary documents ordered by generation date descending for export.
    List<NoDesSummary> findAllByOrderByGeneratedAtDesc();
}
