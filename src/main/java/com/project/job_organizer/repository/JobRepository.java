package com.project.job_organizer.repository;

import com.project.job_organizer.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job,Integer> {

   List<Job> findByTitleContaining(String keyword);

}
