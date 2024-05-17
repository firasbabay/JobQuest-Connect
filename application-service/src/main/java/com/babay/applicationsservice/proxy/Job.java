package com.babay.applicationsservice.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;


    private String position;

    private String jobDescription;

    private String location;

    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    private String companyName;

    @ElementCollection(fetch = FetchType.EAGER)

    private List<String> skills;


}
