package com.babay.Authentication.service;

import com.babay.Authentication.model.Account;
import com.babay.Authentication.model.UserCenter;
import com.babay.Authentication.model.UserGeneral;
import com.babay.Authentication.model.UserSeeker;
import com.babay.Authentication.proxy.*;
import com.babay.Authentication.repository.UserRepo;
import com.sun.jdi.request.StepRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service(value = "userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService , UserService {
    @Autowired
    private JobCenterProxy centerProxy;
    @Autowired
    private SeekerProxy seekerProxy;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;
    @Autowired
    private NotificationProxy notificationProxy;


    public UserDetails loadUserByUsername(String username) {
        Account user = userRepo.findByUsername(username);
        if (user == null ){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword() , getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
    public List<Account> findAll() {
        List<Account> list = new ArrayList<>();
        userRepo.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public ResponseEntity delete(String username) {
        Account user = userRepo.findByUsername(username);
        userRepo.delete(user);
        if (Account.Role.SEEKER.equals(user.getRole())){
            seekerProxy.deleteSeeker(username , user.getUsername());
        } else if (Account.Role.JOB_CENTER.equals(user.getRole())) {
            centerProxy.deleteJobCenter(username, user.getUsername());
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public Account findOne(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public Account findById(long id) {
        Optional<Account> optionalUser = userRepo.findById(id);
        return optionalUser.orElse(null);
    }

    @Override
    public ResponseEntity update(String loggedUser, UserGeneral newUser) {
        String username = newUser.getUsername();
        Account user =findByUsername(username);
        if (!loggedUser.equals(newUser.getUsername())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (newUser.getPassword() != null){
            user.setPassword(bcryptEncoder.encode(newUser.getPassword()));
        }
        user.setEmail(newUser.getEmail());
        if (newUser instanceof UserSeeker){
            user.setRole(Account.Role.SEEKER);
                if (user.getRole() != Account.Role.SEEKER){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                UserSeeker newUs = (UserSeeker) newUser ;
                seekerProxy.changeJobSeeker(loggedUser , username ,new Seeker(newUs.getUsername(), newUs.getFirstName(), newUs.getLastName(), newUs.getEmail(),
                        newUs.getCity(), newUs.getBirth(), newUs.getSkills()));

        } else if (newUser instanceof UserCenter) {
            user.setRole(Account.Role.JOB_CENTER);
            if (user.getRole() != Account.Role.JOB_CENTER)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            UserCenter newUc = (UserCenter) newUser;
            centerProxy.updateJobCenter(loggedUser, username,
                    new JobCenter(newUc.getCenterName(), newUc.getUsername(), newUc.getEmail()));

        }
        userRepo.save(user);
        return ResponseEntity.ok().build();
    }

    @Override
    public Account findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                Account user = userRepo.findByUsername(username);
                if (user == null ){
                    throw new UsernameNotFoundException("Invalid username or password.");
                }
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword() , getAuthority());
            }

        };
    }

    @Override
    public ResponseEntity<Account> save(UserGeneral user) {
        Account newUser = new Account();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        if (user instanceof UserSeeker)
            newUser.setRole(Account.Role.SEEKER);
        else if (user instanceof UserCenter)
            newUser.setRole(Account.Role.JOB_CENTER);
        newUser.setEmail(user.getEmail());

        if (!userRepo.existsByUsername(user.getUsername())) {
            dispatchUser(user);
            Account newUserSave = userRepo.save(newUser);
            return ResponseEntity.ok().body(newUserSave);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    public void dispatchUser(UserGeneral user) {
        if (user instanceof UserSeeker) {
            UserSeeker us = (UserSeeker) user;
            Seeker newSeeker = new Seeker(us.getUsername(), us.getFirstName(), us.getLastName(),
                    us.getEmail(), us.getCity(), us.getBirth(), us.getSkills());
            seekerProxy.createInstance(newSeeker);
            sendEmail(new Notification(us.getEmail(), "Benvenuto nel portale di lavoro più famoso al mondo!",
                    "Benvenuto caro Seeker " + us.getFirstName(), null));
        } else if (user instanceof UserCenter) {
            UserCenter uc = (UserCenter) user;
            centerProxy.createJobCenter(new JobCenter(uc.getCenterName(), uc.getUsername(), uc.getEmail()));
            sendEmail(new Notification(uc.getEmail(), "Benvenuto nel portale di lavoro più famoso al mondo!",
                    "Benvenuto caro Center " + uc.getCenterName(), null));
        }

    }
    @Bean
    public BCryptPasswordEncoder bcryptEncoder(){
        return new BCryptPasswordEncoder();
    }

    public void sendEmail(Notification notificationEntity) {
        notificationProxy.sendNotification(notificationEntity);
    }

}
