package com.project.job_organizer.service;

import com.project.job_organizer.model.Job;
import com.project.job_organizer.repository.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }


    public List<Job> getJobsByUserId(Long userId) {
        return jobRepository.findByUserId(userId);
    }

    // Ottieni un job specifico di un utente specifico
    public Job getJobByIdAndUserId(Long id, Long userId) {
        return jobRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }


    public List<Job> getJobsByTitleAndUserId(String title, Long userId) {
        return jobRepository.findByTitleContainingAndUserId(title, userId);
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

   @Transactional
    public void deleteJobByIdAndUserId(Long id, Long userId) {
        if (!jobRepository.existsByIdAndUserId(id, userId)) {
            throw new RuntimeException("Job not found with id: " + id);
        }
        jobRepository.deleteByIdAndUserId(id, userId);
    }


    public Job updateJob(Long id, Job jobDetails, Long userId) {
        Job existingJob = getJobByIdAndUserId(id, userId);

        if (jobDetails.getTitle() != null) {
            existingJob.setTitle(jobDetails.getTitle());
        }
        if (jobDetails.getCompany() != null) {
            existingJob.setCompany(jobDetails.getCompany());
        }
        if (jobDetails.getLocation() != null) {
            existingJob.setLocation(jobDetails.getLocation());
        }
        if (jobDetails.getDescription() != null) {
            existingJob.setDescription(jobDetails.getDescription());
        }
        if (jobDetails.getStatus() != null) {
            existingJob.setStatus(jobDetails.getStatus());
        }
        if (jobDetails.getType() != null) {
            existingJob.setType(jobDetails.getType());
        }
        if (jobDetails.getSalary() != null) {
            existingJob.setSalary(jobDetails.getSalary());
        }
        if (jobDetails.getStartingDate() != null) {
            existingJob.setStartingDate(jobDetails.getStartingDate());
        }
        if (jobDetails.getPros() != null) {
            existingJob.setPros(jobDetails.getPros());
        }
        if (jobDetails.getCons() != null) {
            existingJob.setCons(jobDetails.getCons());
        }
        if (jobDetails.getUrl() != null) {
            existingJob.setUrl(jobDetails.getUrl());
        }

        return jobRepository.save(existingJob);
    }
}