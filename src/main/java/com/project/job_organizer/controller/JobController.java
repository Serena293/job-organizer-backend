package com.project.job_organizer.controller;

import com.project.job_organizer.model.Job;

import com.project.job_organizer.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<Job>> getUserJobs(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();

        List<Job> userJobs = jobService.getJobsByUserId(userId);
        return ResponseEntity.ok(userJobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();

        Job job = jobService.getJobByIdAndUserId(id, userId);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/filter/{keyword}")
    public ResponseEntity<List<Job>> getJobsByTitle(@PathVariable String keyword, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();

        List<Job> jobs = jobService.getJobsByTitleAndUserId(keyword, userId);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        job.setUserId(userPrincipal.getId());

        Job createdJob = jobService.createJob(job);
        return ResponseEntity.ok(createdJob);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();

        jobService.deleteJobByIdAndUserId(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();

        Job updatedJob = jobService.updateJob(id, job, userId);
        return ResponseEntity.ok(updatedJob);
    }
}