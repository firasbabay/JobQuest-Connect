package com.babay.applicationsservice.service;

import com.babay.applicationsservice.Model.*;
import com.babay.applicationsservice.Repository.ApplicationRepository;
import com.babay.applicationsservice.proxy.JobCenterProxy;
import com.babay.applicationsservice.proxy.JobProxy;
import com.babay.applicationsservice.proxy.NotificationProxy;
import com.babay.applicationsservice.proxy.SeekerProxy;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationService {
    @Autowired
    private final ApplicationRepository applicationRepository;

    @Autowired
    private JobProxy jobProxy;

    @Autowired
    private JobCenterProxy jobCenterProxy;

    @Autowired
    private SeekerProxy seekerProxy;

    @Autowired
    private NotificationProxy notificationProxy;


    @Value("${pass}")
    private String pass;

    public ResponseEntity<List<Application>> getCentersApplications(String loggedUser,  String username) {
        if (!username.equals(loggedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ResponseEntity<JobCenter> response = jobCenterProxy.getJobCenter(pass, username);
        if (!(response.getStatusCodeValue() == 200)) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(applicationRepository.findAllByCenterUsername(username));
    }


    public ResponseEntity<List<Application>> getCentersApplications(
             String loggedUser,  String username,
             long jobId) {
        if (!username.equals(loggedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // MUST BE CENTER
        ResponseEntity<JobCenter> response = jobCenterProxy.getJobCenter(pass, username);
        if (!(response.getStatusCodeValue() == 200)) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(applicationRepository.findAllByJobId(jobId));
    }



    public ResponseEntity<List<Application>> getApplications( String loggedUser,
                                                                    String username) {
        if (!username.equals(loggedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //MUST BE SEEKER
        ResponseEntity<Seeker> response = seekerProxy.getJobSeeker(pass, username);
        if (!(response.getStatusCodeValue() == 200)) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(applicationRepository.findAllByUsername(username));
    }



    public ResponseEntity<Application> createApplication( String loggedUser, String role, @PathVariable String username, Application application) throws URISyntaxException {
        if (!username.equals(loggedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Job relatedJob;
        if (!"SEEKER".equals(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            relatedJob = jobProxy.getJob(loggedUser, application.getUsername(), application.getJobId());
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if((applicationRepository.findByUsernameAndJobId(application.getUsername(), application.getJobId())) == null) {
            application.setDateCreation(new Date());
            application.setUsername(username);
            application.setJobId(application.getJobId());
            application.setCenterUsername(relatedJob.getUsername());
            Application a = applicationRepository.save(application);
            ResponseEntity<JobCenter> response = jobCenterProxy.getJobCenter(pass, relatedJob.getUsername());
            String receiverEmail = "";
            if (response.getStatusCodeValue() == 200)
                receiverEmail = Objects.requireNonNull(response.getBody()).getEmail();
            notificationProxy.sendNotification(new Notification(receiverEmail, "Seeker applied to your job ",
                    "Seeker " + username + " Applied to your job " + relatedJob.getJobDescription(), username));
            return ResponseEntity.created(new URI("/api/seekers/" + username + "/applications/" + a.getId())).body(a);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    public ResponseEntity<Application> getApplication( String loggedUser, String username,  long applicationId) {
        if (!username.equals(loggedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (!applicationOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Application application = applicationOpt.get();
        if (username.equals(application.getUsername())) {
            return ResponseEntity.ok(application);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



    public ResponseEntity deleteAllApplicationsByUsername( String loggedUser, String username) {
        if (!username.equals(loggedUser))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        applicationRepository.deleteAllByUsername(username);
        return ResponseEntity.ok().build();
    }



    public ResponseEntity deleteAllApplicationsByJobId( String loggedUser, String username,  long jobId) {
        if (!username.equals(loggedUser))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        applicationRepository.deleteAllByJobId(jobId);
        return ResponseEntity.ok().build();
    }



    public ResponseEntity<Application> deleteApplication( String loggedUser, String username,  long applicationId) {
        if (!username.equals(loggedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (!applicationOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Application application = applicationOpt.get();
        if (!username.equals(application.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        applicationRepository.deleteById(applicationId);
        return ResponseEntity.ok().build();
    }



    public ResponseEntity<Application> updateApplication( String loggedUser, String role,  String username, long applicationId,  Application application) {
        if (!username.equals(loggedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!"SEEKER".equals(role)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (application.getId() != applicationId || !username.equals(application.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Job relatedJob = jobProxy.getJob(loggedUser, application.getUsername(), application.getJobId());
        Date dateCreation = applicationRepository.findById(applicationId).get().getDateCreation();
        application.setDateCreation(dateCreation);
        applicationRepository.save(application);
        return ResponseEntity.ok(application);
    }
}
