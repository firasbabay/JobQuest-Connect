package com.babay.advisor.model;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
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
