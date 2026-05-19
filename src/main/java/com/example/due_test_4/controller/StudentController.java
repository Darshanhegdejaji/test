package com.example.due_test_4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import com.example.due_test_4.service.StudentService;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import com.example.due_test_4.entity.*;
import com.example.due_test_4.dto.*;
import java.security.Principal;

@RestController()
@RequestMapping(value = "/api/students")
public class StudentController {

    @Autowired()
    private StudentService studentService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('STUDENT','SUPER ADMIN')")
    public ResponseEntity<Student> createStudent(@RequestBody Student entity) {
        return ResponseEntity.ok(studentService.createStudent(entity));
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('STUDENT','DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','DEPARTMENT ADMIN','SUPER ADMIN')")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        return studentService.getStudentById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT','SUPER ADMIN')")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @RequestBody Student entity) {
        Student updated = studentService.updateStudent(id, entity);
        if (updated != null)
            return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    /*
 * Operation    : Search Students
 * Comment      : Endpoint for Super Admin to search students by keyword via query parameter.
 */
    @GetMapping(value = "/api/students/search")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<List<Student>> searchStudents(String keyword) {
        return ResponseEntity.ok(studentService.searchStudents(keyword));
    }

    /*
 * Operation    : Get Student Details
 * Comment      : Endpoint to retrieve detailed profile information of a specific student including personal and enrollment details.
 */
    @GetMapping(value = "/api/students/{studentId}")
    @PreAuthorize("hasAnyRole('SUPER ADMIN','DEPARTMENT ADMIN')")
    public ResponseEntity<Student> getStudentDetails(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getStudentDetails(studentId));
    }

    /*
 * Operation    : Get Student-wise Due Report
 * Comment      : Endpoint for Super Admin to retrieve a student-wise due report summarizing pending dues, cleared dues, and clearance status.
 */
    @GetMapping(value = "/api/reports/student-wise-due")
    @PreAuthorize("hasRole('SUPER ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getStudentWiseDueReport(Principal principal) {
        return ResponseEntity.ok(studentService.getStudentWiseDueReport(principal));
    }
}
