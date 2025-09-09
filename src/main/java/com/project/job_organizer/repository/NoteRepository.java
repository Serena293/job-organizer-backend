package com.project.job_organizer.repository;

import com.project.job_organizer.model.NotesEntity;
import com.project.job_organizer.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<NotesEntity, Long> {
    List<NotesEntity> findAllByUser(UserEntity user);
    List<NotesEntity> findAllByUserId(Long userId);
}
