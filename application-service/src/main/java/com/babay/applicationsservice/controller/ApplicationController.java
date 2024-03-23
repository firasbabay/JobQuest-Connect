package com.babay.applicationsservice.controller;

import com.babay.applicationsservice.Model.Application;
import com.babay.applicationsservice.Repository.ApplicationRepository;
import com.babay.applicationsservice.service.ApplicationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService ;

    @RequestMapping(value = "/api/centers/{username}/applications/", method = RequestMethod.GET)
    public ResponseEntity<List<Application>> getCentersApplications(
            @RequestHeader("X-User-Header") String loggedUser, @PathVariable String username) {
        return applicationService.getCentersApplications(loggedUser , username);
    }


    @RequestMapping(value = "/api/centers/{username}/jobs/{jobId}/applications", method = RequestMethod.GET)
    public ResponseEntity<List<Application>> getCentersApplications(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username, @PathVariable long jobId) {
        return applicationService.getCentersApplications(loggedUser , username , jobId);
    }


    @RequestMapping(value = "/api/seekers/{username}/applications/", method = RequestMethod.GET)
    public ResponseEntity<List<Application>> getApplications(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username) {
        return applicationService.getApplications(loggedUser , username) ;
    }


    @RequestMapping(value = "/api/seekers/{username}/applications/", method = RequestMethod.POST)
    public ResponseEntity<Application> createApplication(@RequestHeader("X-User-Header") String loggedUser, @RequestHeader("X-User-Role-Header") String role, @PathVariable String username, @RequestBody Application application) throws URISyntaxException {
        return applicationService.createApplication(loggedUser ,role , username , application);
    }


    @RequestMapping(value = "/api/seekers/{username}/applications/{applicationId}", method = RequestMethod.GET)
    public ResponseEntity<Application> getApplication(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username, @PathVariable long applicationId) {
        return applicationService.getApplication(loggedUser , username , applicationId);
    }


    @RequestMapping(value = "/api/seekers/{username}/applications", method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity deleteAllApplicationsByUsername(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username) {
      return applicationService.deleteAllApplicationsByUsername(loggedUser ,username);
    }


    @RequestMapping(value = "/api/centers/{username}/jobs/{jobId}/applications", method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity deleteAllApplicationsByJobId(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username, @PathVariable long jobId) {
        return applicationService.deleteAllApplicationsByJobId(loggedUser , username , jobId);
    }


    @RequestMapping(value = "/api/seekers/{username}/applications/{applicationId}", method = RequestMethod.DELETE)
    public ResponseEntity<Application> deleteApplication(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username, @PathVariable long applicationId) {
       return applicationService.deleteApplication(loggedUser , username ,applicationId);
    }


    @RequestMapping(value = "/api/seekers/{username}/applications/{applicationId}", method = RequestMethod.PUT)
    public ResponseEntity<Application> updateApplication(@RequestHeader("X-User-Header") String loggedUser, @RequestHeader("X-User-Role-Header") String role, @PathVariable String username, @PathVariable long applicationId, @RequestBody Application application) {
        return applicationService.updateApplication(loggedUser , role , username ,applicationId , application);
    }

}
