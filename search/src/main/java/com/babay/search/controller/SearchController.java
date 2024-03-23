package com.babay.search.controller;

import com.babay.search.Repository.jobsRepository;
import com.babay.search.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class SearchController {
    @Autowired
    private jobsRepository jobsRepository ;
    @GetMapping(value = "/api/jobs/search")
    public ResponseEntity<List<Job>> getJobs(@RequestParam("q") Optional<String> query,
                                             @RequestParam("location") Optional<String> location) {
        String q = "";
        String locationOpt = "";

        if (query.isPresent()) {
            q = query.get();
        }
        if (location.isPresent()) {
            locationOpt = location.get();
        }

        List<Job> result = jobsRepository.findJobByQuery(q, locationOpt);
        return ResponseEntity.ok(result);
    }
}
