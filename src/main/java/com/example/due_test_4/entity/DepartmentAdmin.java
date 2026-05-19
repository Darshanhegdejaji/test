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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity()
@Builder()
@Data()
@NoArgsConstructor()
@AllArgsConstructor()
@Getter()
@Setter()
@Table(name = "department_admins")
public class DepartmentAdmin {

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

    @Column(nullable = false)
    private Boolean isActive;

    @CreationTimestamp()
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp()
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne()
    @JsonIgnore()
    @JoinColumn(name = "departmentId")
    private Department department;
}
