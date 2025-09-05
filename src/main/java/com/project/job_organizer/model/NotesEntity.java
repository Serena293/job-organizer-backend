package com.project.job_organizer.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.SpringVersion;

@Entity
@Data
public class NotesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String noteTitle;
    private String noteContent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
