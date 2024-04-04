package com.babay.Authentication.Services;

import com.babay.Authentication.Model.User;
import com.babay.Authentication.Model.UserGeneral;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface EndService {
    List<User> findAll();

    Optional<User> findByUsername(String username);

    ResponseEntity update(String loggedUser, UserGeneral newUser, String username);

    ResponseEntity delete(String username);
}
