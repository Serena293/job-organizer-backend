package com.project.job_organizer.service;

import com.project.job_organizer.model.Job;
import com.project.job_organizer.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Integer id) {
        return jobRepository.findById(id).orElse(null);
    }

    public List<Job> getJobsByTitle(String title) {
        return jobRepository.findByTitleContaining(title);
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public void deleteJob(Integer id) {
        jobRepository.deleteById(id);
    }
    public Job updateJob(Integer id, Job jobDetails) {
        return jobRepository.findById(id)
                .map(existingJob -> {
                    // Aggiorna solo i campi che sono presenti nel jobDetails
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
                })
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }
}
