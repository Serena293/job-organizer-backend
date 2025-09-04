package com.project.job_organizer.model;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String token;
    private String newPassword;
    private String confirmPassword;
}
