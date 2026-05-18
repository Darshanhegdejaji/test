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
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "dues")
public class Due {

    @Id()
    @GeneratedValue()
    @UuidGenerator()
    private String id;

    @Column(nullable = false)
    private String dueType;

    private Double amount;

    private String remarks;

    @Column(nullable = false)
    private String status;

    private LocalDate dueDate;

    private LocalDate clearedDate;

    @CreationTimestamp()
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp()
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne()
    @JsonIgnore()
    @JoinColumn(name = "studentId")
    private Student student;

    @ManyToOne()
    @JsonIgnore()
    @JoinColumn(name = "departmentId")
    private Department department;

    @OneToMany(mappedBy = "due", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DueAttachment> dueAttachments = new ArrayList<>();
}
