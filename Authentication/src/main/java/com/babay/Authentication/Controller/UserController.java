package com.babay.Authentication.Controller;

import com.babay.Authentication.Model.User;
import com.babay.Authentication.Model.UserGeneral;
import com.babay.Authentication.Services.EndService;
import com.babay.Authentication.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final EndService endService ;

    @GetMapping
    public ApiResponse<List<User>> listUser(){
        return new ApiResponse<>(HttpStatus.OK.value(), "User list fetched successfully.", endService.findAll());
    }
    @GetMapping("/{username}")
    public ResponseEntity getOne(@PathVariable String username){
        return ResponseEntity.ok(endService.findByUsername(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity update(@RequestHeader("X-User-Header") String loggedUser, @RequestBody UserGeneral newUser, @PathVariable String username) {
        System.out.println(newUser.getUsername());
        return endService.update(loggedUser,newUser , username);
    }
    @DeleteMapping("/{username}")
    public ResponseEntity delete(@PathVariable String username) {
        return endService.delete(username);
    }

    @GetMapping("/recover-email/{username}")
    public String recoverEmail(@PathVariable String username) {
        Optional<User> userOptional = endService.findByUsername(username);
        User user =userOptional.get();

        if(user != null) {
            return user.getEmail();
        }
        return null;
    }
}