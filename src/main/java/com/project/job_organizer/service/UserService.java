package com.project.job_organizer.service;

import com.project.job_organizer.model.Role;
import com.project.job_organizer.model.UserDTO;
import com.project.job_organizer.model.UserEntity;
import com.project.job_organizer.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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


}
