package com.babay.seekers.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "seekers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Seekers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String city;
    @Temporal(TemporalType.DATE)
    private Date birth;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> skills;
    public void removeSkill(String skill) {
        skills.remove(skill);
    }

    public void addSkills(List<String> skills) {
        skills.addAll(skills);
    }
}
