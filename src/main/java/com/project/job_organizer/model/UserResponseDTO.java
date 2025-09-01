package com.project.job_organizer.model;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String role;
    private String token;


}
