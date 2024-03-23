package com.babay.Authentication.proxy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobCenter {
    private Long id ;
    private String name ;
    private String username ;
    private String email ;

    public JobCenter(String name, String username, String email) {
        this.name = name;
        this.username = username;
        this.email = email;
    }
}
