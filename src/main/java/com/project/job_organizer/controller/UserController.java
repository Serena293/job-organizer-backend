package com.project.job_organizer.controller;

import com.project.job_organizer.model.LoginDTO;
import com.project.job_organizer.model.UserDTO;
import jakarta.validation.Valid;
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
    @PatchMapping("/me")
    public UserDTO updateCurrentUser(@Valid @RequestBody UserDTO updateDto, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();

        UserEntity updatedUser = userService.updateProfile(
                userId,
                updateDto.getUsername(),
                updateDto.getEmail(),
                updateDto.getPassword()
        );

        UserDTO dto = new UserDTO();
        dto.setUsername(updatedUser.getUsername());
        dto.setEmail(updatedUser.getEmail());
        return dto;
    }


}