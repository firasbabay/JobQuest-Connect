package com.babay.Authentication.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String token ;

    private String refreshToken ;
}
