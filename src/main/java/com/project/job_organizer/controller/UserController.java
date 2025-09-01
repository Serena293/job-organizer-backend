package com.project.job_organizer.controller;

import com.project.job_organizer.model.LoginDTO;
import com.project.job_organizer.model.UserDTO;
import com.project.job_organizer.model.UserEntity;
import com.project.job_organizer.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }




}