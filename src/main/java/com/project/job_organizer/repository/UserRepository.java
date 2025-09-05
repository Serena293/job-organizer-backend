package com.project.job_organizer.repository;

import com.project.job_organizer.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
Optional<UserEntity> findByUsername(String username);
Optional<UserEntity> findByEmail(String email);
Optional<UserEntity> findByUsernameOrEmail(String username, String email);

Optional<UserEntity> findById(Long id);
boolean existsByUsername(String username);
boolean existsByEmail(String email);

}
