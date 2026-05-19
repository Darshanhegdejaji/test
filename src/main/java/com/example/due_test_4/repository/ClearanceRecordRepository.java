package com.example.due_test_4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.example.due_test_4.entity.*;

@Repository()
public interface ClearanceRecordRepository extends JpaRepository<ClearanceRecord, String> {

    // repo_method_id: repo_find_clearance_record_by_student_id | Finds the clearance record associated with the given student ID.
    Optional<ClearanceRecord> findByStudentId(String studentId);

    // repo_method_id: repo_count_clearance_records_total | Count total clearance records across all students.
    @Query(value = "SELECT COUNT(cr) FROM ClearanceRecord cr", nativeQuery = false)
    Long countAllRecords();

    // repo_method_id: repo_sum_cleared_dues | Sum of all clearedDues across all clearance records.
    @Query(value = "SELECT COALESCE(SUM(cr.clearedDues), 0) FROM ClearanceRecord cr", nativeQuery = false)
    Long sumClearedDues();

    // repo_method_id: repo_sum_pending_dues | Sum of all pendingDues across all clearance records.
    @Query(value = "SELECT COALESCE(SUM(cr.pendingDues), 0) FROM ClearanceRecord cr", nativeQuery = false)
    Long sumPendingDues();

    // repo_method_id: repo_sum_total_dues | Sum of all totalDues across all clearance records.
    @Query(value = "SELECT COALESCE(SUM(cr.totalDues), 0) FROM ClearanceRecord cr", nativeQuery = false)
    Long sumTotalDues();

    // repo_method_id: repo_count_cleared_overall_status | Count clearance records by overallStatus value (e.g. CLEARED, PENDING).
    Long countByOverallStatus(String overallStatus);

    // repo_method_id: repo_find_all_clearance_records | Retrieves all ClearanceRecords ordered by creation date ascending for trend analysis.
    List<ClearanceRecord> findAllByOrderByCreatedAtAsc();
}
