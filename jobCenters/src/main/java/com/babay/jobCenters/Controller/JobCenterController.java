package com.babay.jobCenters.Controller;

import com.babay.jobCenters.Model.jobcenter;
import com.babay.jobCenters.Service.JobCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/centers")
@RequiredArgsConstructor
public class JobCenterController {
    @Autowired
    private JobCenterService jobCenterService ;

    @GetMapping
    public List<jobcenter> getAllCenters(){
        return jobCenterService.getAllCenter();
    }
    @PostMapping
    public ResponseEntity<jobcenter> createJobCenter(@RequestBody jobcenter jobcenter) throws URISyntaxException {
        jobcenter createdCenter = jobCenterService.createJobCenter(jobcenter);
        return ResponseEntity.created(new URI("/api/centers" + createdCenter.getId())).body(jobcenter);
    }
    @DeleteMapping(value = "/{username}")
    public ResponseEntity<jobcenter> deleteJobCenter(@RequestHeader("X-User-Header") String loggedUser,
                                                           @PathVariable String username){
        return jobCenterService.deleteJobCenter(loggedUser ,username);
    }
    @GetMapping(value = "/{username}")
    public ResponseEntity<jobcenter> getJobCenter(@PathVariable String username){
        return jobCenterService.getJobCenter(username);
    }
    @PostMapping(value = "/{username}")

    public ResponseEntity<jobcenter> updateJobCenter(@RequestHeader("X-User-Header") String loggedUser,@PathVariable String username ,@RequestBody jobcenter jobcenter){
        return jobCenterService.updateJobCenter(loggedUser , username , jobcenter);
    }

}
