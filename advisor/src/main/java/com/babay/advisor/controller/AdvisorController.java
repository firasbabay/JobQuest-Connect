package com.babay.advisor.controller;

import com.babay.advisor.Proxy.SeekerProxy;
import com.babay.advisor.model.Job;
import com.babay.advisor.model.Seeker;
import com.babay.advisor.repository.JobsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AdvisorController {
    @Autowired
    private JobsRepository jobsRepository;

    @Autowired
    private SeekerProxy seekerProxy;

    
    @RequestMapping(value = "api/seekers/{username}/suggestions", method = RequestMethod.GET)
    public ResponseEntity<List<Job>> getAllJobs(@RequestHeader("X-User-Header") String loggedUser,
                                                @PathVariable String username) {
        if (!username.equals(loggedUser))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Seeker user = seekerProxy.getSeeker(loggedUser, username);
        if (user == null)
            return ResponseEntity.notFound().build();

        List<Job> jobs = jobsRepository.findAll();

        if (jobs == null)
            return ResponseEntity.ok().build();

        List<String> skills = user.getSkills();

        if (skills == null)
            return ResponseEntity.ok().build();

        Map<Long, List<Job>> resultMatched = new HashMap<>();

        for (Job jobEntity : jobs) {
            long matches = skills.stream().distinct().filter(jobEntity.getSkills()::contains).count();
            if (matches > 0) {
                if (resultMatched.get(Long.valueOf(matches)) == null)
                    resultMatched.put(matches, new ArrayList<>());
                if (jobEntity.getLocation().equals(user.getCity()))
                    resultMatched.get(Long.valueOf(matches)).add(0, jobEntity);
                else
                    resultMatched.get(Long.valueOf(matches)).add(jobEntity);
            }
        }

        List<Long> keys = new ArrayList<>(resultMatched.keySet());
        Collections.sort(keys);
        List<Job> result = new ArrayList<>();
        for (int i = keys.size() - 1; i >= 0; i--) {
            result.addAll(resultMatched.get(keys.get(i)));
        }

        return ResponseEntity.ok(result);
        // return ResponseEntity.ok(jobsRepository.findAllByLocation(user.getCity()));
    }
}
