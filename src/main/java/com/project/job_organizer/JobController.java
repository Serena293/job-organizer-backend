package com.project.job_organizer;

import com.project.job_organizer.model.Job;
import com.project.job_organizer.service.JobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/all")
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public Job getJobById(@PathVariable Integer id) {
        return jobService.getJobById(id);
    }

    @GetMapping("/filter/{keyword}")
    public List<Job> getJobsByTitle(@PathVariable String keyword) {
        return jobService.getJobsByTitle(keyword);
    }

    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Integer id) {
        jobService.deleteJob(id);
    }

    @PatchMapping("/{id}")
    public Job updateJob(@PathVariable Integer id, @RequestBody Job job) {
        return jobService.updateJob(id, job);
    }
}
