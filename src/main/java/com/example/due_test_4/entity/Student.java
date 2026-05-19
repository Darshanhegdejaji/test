package com.example.due_test_4.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;
import java.util.ArrayList;

@Entity()
@Builder()
@Data()
@NoArgsConstructor()
@AllArgsConstructor()
@Getter()
@Setter()
@Table(name = "students")
public class Student {

    @Id()
    @GeneratedValue()
    @UuidGenerator()
    private String id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String enrollmentNumber;

    @Column(nullable = false)
    private Integer graduationYear;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Boolean isCleared;

    @CreationTimestamp()
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp()
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne()
    @JoinColumn(name = "studentId")
    private ClearanceRecord clearanceRecord;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Due> dues = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoDesSummary> noDesSummarys = new ArrayList<>();
}
