package com.babay.Authentication.proxy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private String destination ;
    private String subject ;
    private String body ;
    private String username ;
}
