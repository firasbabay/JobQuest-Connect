package com.babay.applicationsservice.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table (name="application")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    private String username;
    @Temporal(TemporalType.DATE)
    private Date dateCreation ;
    private long jobId ;
    private String centerUsername ;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Date getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
    public long getJobId() {
        return jobId;
    }
    public void setJobId(long jobId) {
        this.jobId = jobId;
    }
    public String getCenterUsername() {
        return centerUsername;
    }
    public void setCenterUsername(String centerUsername) {
        this.centerUsername = centerUsername;
    }

}
