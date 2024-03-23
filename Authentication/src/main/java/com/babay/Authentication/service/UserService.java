package com.babay.Authentication.service;

import com.babay.Authentication.model.Account;
import com.babay.Authentication.model.UserGeneral;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public interface UserService {
    ResponseEntity<Account> save(UserGeneral user);
    List<Account> findAll();
    ResponseEntity delete(String username);
    Account findOne(String username);
    Account findById(long id);
    ResponseEntity update(String loggedUser, UserGeneral newUser);
    Account findByUsername(String username);
    public UserDetailsService userDetailsService();
    BCryptPasswordEncoder bcryptEncoder();
}
