package com.babay.notifications.model;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private String destination ;
    private String subject ;
    private String body ;

    private String username ;
}

