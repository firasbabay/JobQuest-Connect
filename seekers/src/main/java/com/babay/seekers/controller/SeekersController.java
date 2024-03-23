package com.babay.seekers.controller;

import com.babay.seekers.model.Seekers;
import com.babay.seekers.service.SeekersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("api/seekers")
public class SeekersController {
    @Autowired
    private SeekersService seekersService ;

    @PostMapping
    public ResponseEntity<Seekers> createInstance(@RequestBody Seekers seekers) throws URISyntaxException {
        Seekers createdSeeker = seekersService.createSeeker(seekers);
        return ResponseEntity.created(new URI("/api/seekers/" + createdSeeker.getId())).body(createdSeeker);
    }
    @GetMapping("/{username}")
    public ResponseEntity<Seekers> getJobSeeker( @RequestHeader(name = "X-User-Header", required = false)String loggedUser,
                                                     @PathVariable String username) {
        return seekersService.getJobSeeker(loggedUser, username);
    }
    @PutMapping("/{username}")
    public ResponseEntity changeJobSeeker(@RequestHeader(name="X-User-Header" , required = false) String loggedUser,
                                          @PathVariable String username, @RequestBody Seekers seeker) {
        return seekersService.changeJobSeeker(loggedUser, username, seeker);
    }
    @GetMapping("/{username}/skills")
    public ResponseEntity<List<String>> getSkills(@RequestHeader(name="X-User-Header" , required = false) String loggedUser,
                                                  @PathVariable String username) throws URISyntaxException {
        return seekersService.getSkills(loggedUser, username);
    }
    @PutMapping("/{username}/skills")
    public ResponseEntity<List<String>> addSkills(@RequestHeader(name="X-User-Header" , required = false) String loggedUser,
                                                  @PathVariable String username, @RequestBody List<String> newSkills) throws URISyntaxException{
        return seekersService.addSkills(loggedUser, username , newSkills );
    }
    @DeleteMapping("/{username}/skills/{skill}")
    public ResponseEntity deleteSkill(@RequestHeader(name="X-User-Header" , required = false) String loggedUser, @PathVariable String username,
                                      @PathVariable String skill) throws URISyntaxException{
        return seekersService.deleteSkill(loggedUser ,username , skill);
    }
    @DeleteMapping("/{username}")
    public ResponseEntity deleteSeeker(@RequestHeader(name = "X-User-Header", required = false) String loggedUser,
                                       @PathVariable String username) throws URISyntaxException {
        return seekersService.deleteSeeker(loggedUser, username);
    }

}
