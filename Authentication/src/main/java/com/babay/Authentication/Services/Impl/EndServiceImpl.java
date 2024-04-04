package com.babay.Authentication.Services.Impl;

import com.babay.Authentication.Model.*;
import com.babay.Authentication.Services.EndService;
import com.babay.Authentication.proxy.JobCenter;
import com.babay.Authentication.proxy.JobCenterProxy;
import com.babay.Authentication.proxy.Seeker;
import com.babay.Authentication.proxy.SeekerProxy;
import com.babay.Authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EndServiceImpl implements EndService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder ;
    private final JobCenterProxy centerProxy;
    private final SeekerProxy seekerProxy;

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);
        return list ;

    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public ResponseEntity update(String loggedUser, UserGeneral newUser , String username) {
        username = newUser.getUsername();
        Optional<User> userOptional = findByUsername(username);
        if (!loggedUser.equals(newUser.getUsername())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userOptional.get();
        if (newUser.getPassword() != null){
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        user.setEmail(newUser.getEmail());
        if (newUser instanceof UserSeeker){
            user.setRole(Role.SEEKER);
            if (user.getRole() != Role.SEEKER){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            UserSeeker newUs = (UserSeeker) newUser ;
            seekerProxy.changeJobSeeker(loggedUser , username ,new Seeker(newUs.getUsername(), newUs.getFirstName(), newUs.getLastName(), newUs.getEmail(),
                    newUs.getCity(), newUs.getBirth(), newUs.getSkills()));

        } else if (newUser instanceof UserCenter) {
            user.setRole(Role.JOB_CENTER);
            if (user.getRole() != Role.JOB_CENTER)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            UserCenter newUc = (UserCenter) newUser;
            centerProxy.updateJobCenter(loggedUser, username,
                    new JobCenter(newUc.getCenterName(), newUc.getUsername(), newUc.getEmail()));

        }
        userRepository.save(user);
        return ResponseEntity.ok().build();


    }

    @Override
    public ResponseEntity delete(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user= userOptional.get();
        userRepository.delete(user);
        if (Role.SEEKER.equals(user.getRole())){
            seekerProxy.deleteSeeker(username , user.getUsername());
        }else if(Role.JOB_CENTER.equals(user.getRole())){
            centerProxy.deleteJobCenter(username,user.getUsername());
        }
        return ResponseEntity.ok().build();
    }

}
