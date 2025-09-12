package com.project.job_organizer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name ="jobs")
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String company;
    @NotNull
    private String location;
    @Column(length = 2000)
    private String description;
    @NotNull
    private String status;
    private Type type;
    private String salary;
    private String startingDate;

    private String pros;
    private  String cons;

    private String url;
    @Column(name = "user_id", nullable = false)
    private Long userId;


}

