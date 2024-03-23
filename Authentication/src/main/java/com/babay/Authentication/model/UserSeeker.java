package com.babay.Authentication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSeeker extends UserGeneral{
    private String firstName;
    private String lastName;
    private Date birth;
    private String city;
    private List<String> skills;
}
