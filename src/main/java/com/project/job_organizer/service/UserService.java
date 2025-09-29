package com.project.job_organizer.service;

import com.project.job_organizer.model.Role;
import com.project.job_organizer.model.UserDTO;
import com.project.job_organizer.model.UserEntity;
import com.project.job_organizer.repository.UserRepository;
import com.project.job_organizer.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    public UserEntity registerUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        UserEntity user = new UserEntity();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setRole(Role.ROLE_USER);
        user.setUsername(userDTO.getUsername());
        user.setDocuments(new ArrayList<>());
        user.setNotes(new ArrayList<>());

        UserEntity savedUser = userRepository.save(user);

        // invio email non bloccante
        sendWelcomeEmail(savedUser.getEmail(), savedUser.getFirstName());

        return savedUser;
    }

    public UserEntity login(String email, String password) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password or Email");
        }
        return user;
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDTO getUserDtoById(Long id) {
        UserEntity user = getUserById(id);

        UserDTO dto = new UserDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());

        return dto;
    }

    public void sendWelcomeEmail(String userEmail, String firstName) {
        try {
            String subject = "Welcome to Job Organizer!";
            String body = "<h1>Hi " + firstName + "!</h1><p>You have successfully registered!.</p>";
            emailService.sendEmail(userEmail, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to send welcome email to " + userEmail);
        }
    }

    public void sendResetPasswordEmail(String userEmail) {
        try {
            String token = jwtUtil.generateResetPasswordToken(userEmail);

            String subject = "Password Reset Request";
            String resetUrl = "https://job-organizer-frontend.onrender.com/reset-password?token=" + token;
            String body = "<p>Click the link below to reset your password (15 minutes):</p>"
                    + "<a href=\"" + resetUrl + "\">Reset Password</a>";

            emailService.sendEmail(userEmail, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to send reset password email to " + userEmail);
        }
    }

    public void resetPassword(String token, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }
        String email;
        try {
            email = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token");
        }
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public UserEntity updateProfile(Long userId, String newUsername, String newEmail, String newPassword) {
        UserEntity user = getUserById(userId);

        if (newUsername != null && !newUsername.equals(user.getUsername())) {
            if (userRepository.existsByUsername(newUsername)) {
                throw new RuntimeException("Username già in uso");
            }
            user.setUsername(newUsername);
        }

        if (newEmail != null && !newEmail.equals(user.getEmail())) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new RuntimeException("Email già in uso");
            }
            user.setEmail(newEmail);
        }

        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        return userRepository.save(user);
    }
}
