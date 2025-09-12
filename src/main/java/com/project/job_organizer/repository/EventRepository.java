package com.project.job_organizer.repository;

import com.project.job_organizer.model.EventEntity;
import com.project.job_organizer.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long> {


    List<EventEntity> findByUser(UserEntity user);

    List<EventEntity> findByUserAndEventDate(UserEntity user, LocalDate eventDate);

    List<EventEntity> findByUserAndEventTitleContainingIgnoreCase(UserEntity user, String eventTitle);
}
