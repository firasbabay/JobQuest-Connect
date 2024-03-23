package com.babay.search.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    private String username ;
    private String position ;
    private String jobDescription ;
    private String location ;

    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    private String companyName ;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> skills ;
}
