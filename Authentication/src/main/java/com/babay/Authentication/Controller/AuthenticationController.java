package com.babay.Authentication.Controller;

import com.babay.Authentication.Model.User;
import com.babay.Authentication.Model.UserGeneral;
import com.babay.Authentication.Services.AuthenticationService;
import com.babay.Authentication.dto.JwtAuthenticationResponse;
import com.babay.Authentication.dto.RefreshTokenRequest;
import com.babay.Authentication.dto.SigninRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService ;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody UserGeneral user){
        return ResponseEntity.ok(authenticationService.signup(user).getBody());
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest signinRequest){
        return  ResponseEntity.ok(authenticationService.signin(signinRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return  ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }

}
