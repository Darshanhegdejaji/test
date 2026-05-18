package com.example.due_test_4.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;
import java.time.*;
import java.security.Principal;
import com.example.due_test_4.repository.ClearanceRecordRepository;
import com.example.due_test_4.repository.DueRepository;

@Service()
public class StudentService {

    @Autowired()
    private StudentRepository studentRepository;

    public Student createStudent(Student entity) {
        return studentRepository.save(entity);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(id);
    }

    public Student updateStudent(String id, Student entity) {
        if (studentRepository.existsById(id)) {
            entity.setId(id);
            return studentRepository.save(entity);
        }
        return null;
    }

    public void deleteStudent(String id) {
        studentRepository.deleteById(id);
    }

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /*
 * Operation    : Search Students
 * Comment      : Searches students by keyword matching name, email, enrollment number, or department.
 */
    public List<Student> searchStudents(String keyword) {
        List<Student> students = studentRepository.searchStudents(keyword);
        return students;
    }

    /*
 * Operation    : Get Student Details
 * Comment      : Retrieves detailed profile information of a specific student by their ID.
 */
    public Student getStudentDetails(String studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        return student;
    }

    private ClearanceRecordRepository clearanceRecordRepository;

    private DueRepository dueRepository;

    private KeycloakAuthService keycloakAuthService;

    /*
 * Operation    : Get Student-wise Due Report
 * Comment      : Generates a student-wise due report summarizing pending dues, cleared dues, and clearance status for each student. Accessible only by Super Admin.
 */
    public List<Map<String, Object>> getStudentWiseDueReport(Principal principal) {
        String userId = keycloakAuthService.getUserId(principal);
        List<Student> students = studentRepository.findAllByOrderByCreatedAtDesc();
        List<Map<String, Object>> report = new java.util.ArrayList<>();
        for (Student student : students) {
            Map<String, Object> entry = new java.util.LinkedHashMap<>();
            entry.put("studentId", student.getId());
            entry.put("firstName", student.getFirstName());
            entry.put("lastName", student.getLastName());
            entry.put("email", student.getEmail());
            entry.put("enrollmentNumber", student.getEnrollmentNumber());
            entry.put("department", student.getDepartment());
            entry.put("graduationYear", student.getGraduationYear());
            entry.put("isCleared", student.getIsCleared());
            ClearanceRecord clearanceRecord = clearanceRecordRepository.findByStudentId(student.getId()).orElse(null);
            if (clearanceRecord != null) {
                entry.put("overallStatus", clearanceRecord.getOverallStatus());
                entry.put("totalDues", clearanceRecord.getTotalDues());
                entry.put("clearedDues", clearanceRecord.getClearedDues());
                entry.put("pendingDues", clearanceRecord.getPendingDues());
                entry.put("approvedAt", clearanceRecord.getApprovedAt());
            } else {
                Integer pendingCount = dueRepository.countByStudentIdAndStatus(student.getId(), "PENDING");
                Integer clearedCount = dueRepository.countByStudentIdAndStatus(student.getId(), "CLEARED");
                List<Due> allDues = dueRepository.findByStudentId(student.getId());
                entry.put("overallStatus", "NO_CLEARANCE_RECORD");
                entry.put("totalDues", allDues.size());
                entry.put("clearedDues", clearedCount);
                entry.put("pendingDues", pendingCount);
                entry.put("approvedAt", null);
            }
            report.add(entry);
        }
        return report;
    }
}
