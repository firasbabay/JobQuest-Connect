package com.babay.job.Controller;

import com.babay.job.Model.Job;
import com.babay.job.Service.JobService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/centers/{username}/jobs")
public class JobController {
    @Autowired
    private JobService jobService ;

    @GetMapping
    public ResponseEntity<List<Job>> getJobs(@PathVariable String username){
        return jobService.getJobs(username);
    }
    @PostMapping
    public ResponseEntity<Job> CreateJob(@RequestHeader(name = "X-User-Header", required = false) String loggedUser, @RequestHeader("X-User-Role-Header") String role , @PathVariable String username ,@RequestBody Job job) throws URISyntaxException {
        return jobService.CreateJob(loggedUser , role ,username, job);
    }
    @DeleteMapping
    public ResponseEntity deleteAllJobByUsername(@RequestHeader("X-User-Header") String loggedUser , @PathVariable String username){
        return jobService.deleteAllJobUsername(loggedUser , username);
    }
    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJob(@RequestHeader("X-User-Header")String loggedUser, @PathVariable String username, @PathVariable Long jobId ){
        return jobService.getJob(loggedUser , username , jobId);
    }
    @DeleteMapping("/{jobId}")
    public ResponseEntity<Job> deleteJob(@RequestHeader("X-User-Header") String loggedUser,
                                         @PathVariable String username,
                                         @PathVariable long jobId){
        return jobService.deleteJob(loggedUser , username , jobId);
    }
    @PutMapping("/{jobId}")
    public ResponseEntity<Job> updateJob(@RequestHeader("X-User-Header") String loggedUser,
                                         @RequestHeader("X-User-Role-Header") String role,
                                         @PathVariable String username,
                                         @PathVariable Long jobId,
                                         @RequestBody Job job){
        return jobService.updateJob(loggedUser,role ,username,jobId ,job);
    }



}
