package com.babay.Authentication.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserSeeker.class, name = "SEEKER"),
        @JsonSubTypes.Type(value = UserCenter.class, name = "JOB_CENTER")
})
public class UserGeneral {
    private long id ;
    private String username;
    private String password ;
    private String email ;
}
