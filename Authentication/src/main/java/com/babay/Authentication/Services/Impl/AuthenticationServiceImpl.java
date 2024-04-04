package com.babay.Authentication.Services.Impl;

import com.babay.Authentication.Model.*;
import com.babay.Authentication.Services.AuthenticationService;
import com.babay.Authentication.Services.JWTService;
import com.babay.Authentication.dto.JwtAuthenticationResponse;
import com.babay.Authentication.dto.RefreshTokenRequest;
import com.babay.Authentication.dto.SigninRequest;
import com.babay.Authentication.proxy.*;
import com.babay.Authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final AuthenticationManager authenticationManager ;
    private final JWTService jwtService ;
    private final JobCenterProxy centerProxy;
    private final SeekerProxy seekerProxy;
    private final NotificationProxy notificationProxy;

    public ResponseEntity<User> signup(UserGeneral user){
        User NewUser = new User();
        NewUser.setUsername(user.getUsername());
        NewUser.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user instanceof UserSeeker){
            NewUser.setRole(Role.SEEKER);
        }else if (user instanceof UserCenter){
            NewUser.setRole(Role.JOB_CENTER);
        }
        NewUser.setEmail(user.getEmail());
        if(!userRepository.existsByUsername(user.getUsername())){
            dispatchUser(user);
            User newUserSave = userRepository.save(NewUser);
            return ResponseEntity.ok().body(newUserSave);
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }


    }

    private void dispatchUser(UserGeneral user) {
        if (user instanceof UserSeeker){
            UserSeeker us = (UserSeeker) user ;
            Seeker newSeeker =  new Seeker(us.getUsername(), us.getFirstName(), us.getLastName(),
                    us.getEmail(), us.getCity(), us.getBirth(), us.getSkills());
            seekerProxy.createInstance(newSeeker);
            sendEmail(new Notification(us.getEmail(), "Welcome to jobQuest Connect!",
                    "Welcome Dear Seeker " + us.getUsername(), null));
        }else if  (user instanceof UserCenter) {
            UserCenter uc = (UserCenter) user;
            centerProxy.createJobCenter(new JobCenter(uc.getCenterName(), uc.getUsername(), uc.getEmail()));
            sendEmail(new Notification(uc.getEmail(), "Welcome to jobQuest Connect!",
                    "Welcome Dear Center " + uc.getCenterName(), null));
        }
    }
    public void sendEmail(Notification notificationEntity) {
        notificationProxy.sendNotification(notificationEntity);
    }

    public JwtAuthenticationResponse signin(SigninRequest signinRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUsername() , signinRequest.getPassword()));

        var user = userRepository.findByUsername(signinRequest.getUsername()).orElseThrow(()-> new IllegalCallerException("Invalid email or password"));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>() , user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse ;

    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String username = jwtService.extractUsername(refreshTokenRequest.getToken());
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
                var jwt = jwtService.generateToken(user);
                JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

                jwtAuthenticationResponse.setToken(jwt);
                jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
                return jwtAuthenticationResponse ;
            }
        }
        return null ;
    }
}
