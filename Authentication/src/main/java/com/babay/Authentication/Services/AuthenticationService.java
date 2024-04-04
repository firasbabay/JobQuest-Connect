package com.babay.Authentication.Services;

import com.babay.Authentication.Model.User;
import com.babay.Authentication.Model.UserGeneral;
import com.babay.Authentication.dto.JwtAuthenticationResponse;
import com.babay.Authentication.dto.RefreshTokenRequest;
import com.babay.Authentication.dto.SigninRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<User> signup(UserGeneral user);
    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
