package com.babay.jobCenters.Service;

import com.babay.jobCenters.Model.jobcenter;
import com.babay.jobCenters.Repository.JobCenterRepo;
import com.babay.jobCenters.proxy.JobsProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
@Slf4j
public class JobCenterService {
    @Autowired
    private static JobCenterRepo jobCenterRepo ;

    @Autowired
    private JobsProxy jobsProxy ;

    @Value("${pass}")
    private String pass;



    public List<jobcenter> getAllCenter() {
        return jobCenterRepo.findAll();
    }

    public ResponseEntity<jobcenter> createJobCenter(jobcenter jobcenter) throws URISyntaxException {
        jobCenterRepo.save(jobcenter);
        return ResponseEntity.created(new URI("/api/centers" + jobcenter.getId())).body(jobcenter);
    }

    public ResponseEntity<jobcenter> deleteJobCenter(String loggedUser, String username) {
        if (!username.equals(loggedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        jobcenter jobCenter = jobCenterRepo.findByUsername(username);
        if (jobCenter == null) {
            return ResponseEntity.notFound().build();
        }
        jobsProxy.deleteAllByUsername(loggedUser, username);
        jobCenterRepo.delete(jobCenter);
        return ResponseEntity.ok().build();
    }
    public ResponseEntity<jobcenter> getJobCenter( String username) {

        jobcenter jobCenter = jobCenterRepo.findByUsername(username);
        if (jobCenter != null) {
            return ResponseEntity.ok().body(jobCenter);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public ResponseEntity<jobcenter> updateJobCenter(String loggedUser, String username, jobcenter jobcenter) {
        if (!username.equals(loggedUser)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        jobcenter jobCenterOld = jobCenterRepo.findByName(username);
        if (jobCenterOld == null) {
            return ResponseEntity.notFound().build();
        }
        if (!username.equals(jobcenter.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(!checkField(jobcenter)) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
        }
        jobCenterOld.setName(jobcenter.getName());
        jobCenterOld.setEmail(jobcenter.getEmail());
        jobCenterRepo.save(jobCenterOld);
        return ResponseEntity.ok(jobCenterOld);
    }
    private boolean checkField(jobcenter jobcenter){
        if (jobcenter.getUsername() == null){
            return false ;
        }
        if (jobcenter.getEmail() == null){
            return false ;
        }
        if (jobcenter.getName() == null){
            return false ;
        }
        return true ;
    }
}
