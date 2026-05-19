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
@Table(name = "due_attachments")
public class DueAttachment {

    @Id()
    @GeneratedValue()
    @UuidGenerator()
    private String id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne()
    @JsonIgnore()
    @JoinColumn(name = "dueId")
    private Due due;
}
