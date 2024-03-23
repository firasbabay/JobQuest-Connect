package com.babay.job.Model;

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
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id ;

    private String username ;

    private String position ;

    private  String jobDescription;

    private String location ;

    @Temporal(TemporalType.DATE)
    private Date dateCreation ;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> skills  ;
}
