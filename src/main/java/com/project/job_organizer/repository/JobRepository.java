package com.project.job_organizer.repository;

import com.project.job_organizer.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByUserId(Long userId);

    Optional<Job> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    @Query("SELECT j FROM Job j WHERE j.userId = :userId AND LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Job> findByTitleContainingAndUserId(@Param("title") String title, @Param("userId") Long userId);
}