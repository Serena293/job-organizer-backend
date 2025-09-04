package com.project.job_organizer.service;

import com.project.job_organizer.model.Role;
import com.project.job_organizer.model.UserDTO;
import com.project.job_organizer.model.UserEntity;
import com.project.job_organizer.repository.UserRepository;
import com.project.job_organizer.security.JwtUtil;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }
  public UserEntity registerUser(UserDTO userDTO){
        if(userRepository.existsByUsername(userDTO.getUsername())){
            throw new RuntimeException("Username already exists");
        }
        if(userRepository.existsByEmail((userDTO.getEmail()))){
            throw new RuntimeException("Email already exists");
      }

      if(!userDTO.getPassword().equals(userDTO.getConfirmPassword())){
          throw new RuntimeException("Passwords do not match");
      }

      UserEntity user = new UserEntity();
      user.setFirstName(userDTO.getFirstName());
      user.setLastName(userDTO.getLastName());
      user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
      user.setEmail(userDTO.getEmail());
      user.setRole(Role.ROLE_USER);
      user.setUsername(userDTO.getUsername());
      return  userRepository.save(user);
    }

    public UserEntity login (String email, String password){
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid password");
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

    //email service

    public void sendWelcomeEmail(String userEmail, String firstName) {
        String subject = "Welcome to Job Organizer!";
        String body = "<h1>Hi " + firstName + "!</h1><p>You have successfully registered!.</p>";
        try {
            emailService.sendEmail(userEmail, subject, body);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Email not sent");
        }

}

    public void sendResetPasswordEmail(String userEmail) {
        String token = jwtUtil.generateResetPasswordToken(userEmail);

        String subject = "Password Reset Request";
        String resetUrl = "http://localhost:5173/reset-password?token=" + token;

        String body = "<p>Clicca sul link per resettare la password (valido 15 minuti):</p>"
                + "<a href=\"" + resetUrl + "\">Reset Password</a>";

        try {
            emailService.sendEmail(userEmail, subject, body);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Reset password email not sent");
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


}
