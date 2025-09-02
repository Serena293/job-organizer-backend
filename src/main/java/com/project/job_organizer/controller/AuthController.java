package com.project.job_organizer.controller;

import com.project.job_organizer.model.LoginDTO;
import com.project.job_organizer.model.UserDTO;
import com.project.job_organizer.model.UserEntity;
import com.project.job_organizer.model.UserResponseDTO;
import com.project.job_organizer.security.JwtUtil;
import com.project.job_organizer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserDTO userDTO) {
        UserEntity user = userService.registerUser(userDTO);
        String token = jwtUtil.generateToken(user.getEmail());

        UserResponseDTO response = new UserResponseDTO();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setToken(token);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        UserEntity user = userService.login(loginDTO.getEmail(), loginDTO.getPassword());
        String token = jwtUtil.generateToken(user.getEmail());

        UserResponseDTO response = new UserResponseDTO();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setToken(token);

        return ResponseEntity.ok(response);
    }
}
