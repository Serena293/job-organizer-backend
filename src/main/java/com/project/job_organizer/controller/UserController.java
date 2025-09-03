package com.project.job_organizer.controller;

import com.project.job_organizer.model.LoginDTO;
import com.project.job_organizer.model.UserDTO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import com.project.job_organizer.model.UserEntity;
import com.project.job_organizer.service.UserService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/me")
    public UserDTO getCurrentUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        return userService.getUserDtoById(userId);
    }


}