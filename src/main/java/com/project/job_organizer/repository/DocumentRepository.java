package com.project.job_organizer.repository;

import com.project.job_organizer.model.DocumentEntity;
import com.project.job_organizer.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    List<DocumentEntity> findAllByUser(UserEntity user);

    List<DocumentEntity> findAllByUserId(Long userId);


}


