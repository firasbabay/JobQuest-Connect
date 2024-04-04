package com.babay.Authentication.Model;

import lombok.*;

import java.util.Date;
import java.util.List;
@Data
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
