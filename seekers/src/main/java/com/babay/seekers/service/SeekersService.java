package com.babay.seekers.service;

import com.babay.seekers.model.Seekers;
import com.babay.seekers.proxy.ApplicationsProxy;
import com.babay.seekers.repository.SeekersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SeekersService {

    @Autowired
    private SeekersRepository seekersRepository ;
    @Autowired
    private ApplicationsProxy applicationsProxy;

    public Seekers createSeeker(Seekers seekers){
        return seekersRepository.save(seekers);
    }
    public ResponseEntity<Seekers> getJobSeeker(String loggedUser, String username) {
        Seekers seeker = seekersRepository.findByUsername(username);
        if (seeker == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok().body(seeker);
    }
    public ResponseEntity changeJobSeeker(String loggedUser, String username, Seekers seeker) {
        if (!username.equals(loggedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!username.equals(seeker.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (!checkFieldUpdate(seeker)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Seekers seekerOld = seekersRepository.findByUsername(username);
        seekerOld.setUsername(seeker.getUsername());
        seekerOld.setFirstName(seeker.getFirstName());
        seekerOld.setLastName(seeker.getLastName());
        seekerOld.setCity(seeker.getCity());
        seekerOld.setEmail(seeker.getEmail());
        seekerOld.setSkills(seeker.getSkills());
        seekersRepository.save(seekerOld);
        return ResponseEntity.ok(seekerOld);
    }
    private boolean checkFieldUpdate(Seekers newSeeker) {
        if (newSeeker.getUsername() == null) {
            return false;
        }
        if (newSeeker.getFirstName() == null) {
            return false;
        }
        if (newSeeker.getLastName() == null) {
            return false;
        }
        if (newSeeker.getEmail() == null) {
            return false;
        }
        if (newSeeker.getCity() == null) {
            return false;
        }
        if (newSeeker.getBirth() == null) {
            return false;
        }
        return true;
    }
    public ResponseEntity<List<String>> getSkills(String loggedUser, String username) throws URISyntaxException {
        if (!username.equals(loggedUser))
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Seekers seeker = seekersRepository.findByUsername(username);
        if (seeker == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(seeker.getSkills());
    }public ResponseEntity<List<String>> addSkills( String loggedUser, String username,  List<String> newSkills) throws URISyntaxException {
        if (!username.equals(loggedUser))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Seekers seeker = seekersRepository.findByUsername(username);
        if (seeker == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        seeker.setSkills(newSkills);
        seeker = seekersRepository.save(seeker);
        return ResponseEntity.ok(seeker.getSkills());

    }
    public ResponseEntity deleteSkill( String loggedUser,  String username,
                                       String skill) throws URISyntaxException {
        if (!username.equals(loggedUser))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Seekers seeker = seekersRepository.findByUsername(username);
        if (seeker == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        seeker.removeSkill(skill);
        seekersRepository.save(seeker);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity deleteSeeker(String loggedUser, String username) {
        if (!username.equals(loggedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Seekers seeker = seekersRepository.findByUsername(username);
        if (seeker == null) {
            return ResponseEntity.notFound().build();
        }
        applicationsProxy.deleteAllByUsername(loggedUser, username);
        seekersRepository.delete(seeker);
        return ResponseEntity.ok().build();

    }
}
