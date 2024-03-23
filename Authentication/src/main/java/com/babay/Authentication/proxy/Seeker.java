package com.babay.Authentication.proxy;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Seeker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String city;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birth;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> skills;

    public Seeker(String username, String firstName, String lastName, String email, String city, Date birth, List<String> skills) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.birth = birth;
        this.email = email;
        this.skills = skills;
    }
}
