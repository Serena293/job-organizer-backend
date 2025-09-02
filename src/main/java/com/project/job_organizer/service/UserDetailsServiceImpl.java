package com.project.job_organizer.service;

import com.project.job_organizer.controller.UserPrincipal;
import com.project.job_organizer.model.UserEntity;
import com.project.job_organizer.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Searching for user with email: " + email); // DEBUG

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("User not found with email: " + email); // DEBUG
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        System.out.println("User found: " + user.getEmail()); // DEBUG

        return UserPrincipal.create(user);
    }
}