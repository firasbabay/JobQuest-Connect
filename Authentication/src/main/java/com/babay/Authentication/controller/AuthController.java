package com.babay.Authentication.controller;

import com.babay.Authentication.config.JwtService;
import com.babay.Authentication.model.Account;
import com.babay.Authentication.model.ApiResponse;
import com.babay.Authentication.model.AuthToken;
import com.babay.Authentication.model.LoginUser;
import com.babay.Authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/token")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/generate-token")
    public ApiResponse<AuthToken> authenticate(@RequestBody LoginUser loginUser) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
        final Account user = userService.findOne(loginUser.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        return new ApiResponse<>(200, "success",new AuthToken(token, user.getUsername()));
    }

}
