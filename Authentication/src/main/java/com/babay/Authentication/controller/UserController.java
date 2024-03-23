package com.babay.Authentication.controller;

import com.babay.Authentication.model.Account;
import com.babay.Authentication.model.ApiResponse;
import com.babay.Authentication.model.UserGeneral;
import com.babay.Authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ApiResponse<List<Account>> listUser(){
        return new ApiResponse<>(HttpStatus.OK.value(), "User list fetched successfully.", userService.findAll());
    }

    @GetMapping("/users/{username}")
    public ResponseEntity getOne(@PathVariable String username){
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PutMapping("/users/{username}")
    public ResponseEntity update(@RequestHeader("X-User-Header") String loggedUser, @RequestBody UserGeneral newUser, @PathVariable String username) {
        System.out.println(newUser.getUsername());
        return userService.update(loggedUser,newUser);
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity delete(@PathVariable String username) {
        return userService.delete(username);
    }

    @RequestMapping(value="/signup", method = RequestMethod.POST)
    public ResponseEntity<Account> saveNewUser(@RequestBody UserGeneral user){
        return userService.save(user);
    }

    @RequestMapping(value="/recover-email/{username}", method = RequestMethod.GET)
    public String recoverEmail(@PathVariable String username) {
        Account user = userService.findByUsername(username);
        if(user != null) {
            return user.getEmail();
        }
        return null;
    }


}
