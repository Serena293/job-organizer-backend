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
}
