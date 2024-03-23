package com.babay.job.Service;

import com.babay.job.Model.Job;
import com.babay.job.Repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {

    @Autowired
    private JobRepository jobRepository ;
    public ResponseEntity<List<Job>> getJobs(String username) {
        List<Job> jobs = jobRepository.findAllByUsername(username);
        if (jobs == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobs);
    }

    public ResponseEntity<Job> CreateJob(String loggedUser, String role, String username, Job job) throws URISyntaxException {
        //if (!username.equals(loggedUser)){
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        //}
        if(!"JOB_CENTER".equals(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        job.setDateCreation(new Date());
        job.setUsername(username);
        Job j = jobRepository.save(job);
        return ResponseEntity.created(new URI("/api/centers/" + username + "/jobs/" + j.getId())).body(j);


    }

    public ResponseEntity deleteAllJobUsername(String loggedUser, String username) {
        if (!username.equals(loggedUser)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        for (Job job : jobRepository.findAllByUsername(username)){
            deleteJob(loggedUser , username , job.getId());
        }
        return ResponseEntity.ok().build();
    }
    

    public ResponseEntity<Job> deleteJob(String loggedUser, String username, long jobId) {
        if (!username.equals(loggedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Job> jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Job job = jobOpt.get();
        if (!username.equals(job.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        jobRepository.deleteById(jobId);
        return ResponseEntity.ok().build();

    }

    public ResponseEntity<Job> getJob(String loggedUser, String username, Long jobId) {
        Optional<Job> jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Job job = jobOpt.get();
        return ResponseEntity.ok(job);

    }

    public ResponseEntity<Job> updateJob(String loggedUser,String role , String username, Long jobId , Job job ) {
        if (!username.equals(loggedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(!"JOB_CENTER".equals(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!Objects.equals(job.getId(), jobId) || !username.equals(job.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        jobRepository.save(job);
        return ResponseEntity.ok(job);
    }

}
