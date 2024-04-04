package com.babay.notifications.controller;

import com.babay.notifications.CouchDbHelper;
import com.babay.notifications.GmailSender;
import com.babay.notifications.model.Notification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequestMapping(value = "/api/send-notification")
public class NotificationController {


    @PostMapping
    public ResponseEntity<?> sendNotification(@RequestBody Notification notification) {
        if(checkField(notification)) {
            try {
                if (notification.getUsername() == null)
                    GmailSender.sendMessage(notification.getDestination(), notification.getSubject(), notification.getBody() , InputStream.nullInputStream());
                else {
                    InputStream in = CouchDbHelper.getDocument(notification.getUsername());
                    if (in != null)
                        GmailSender.sendMessage(notification.getDestination(), notification.getSubject(), notification.getBody(), in);
                    else
                        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
                }
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
        }
    }

    public boolean checkFieldMail(String destinationEmail) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return destinationEmail.matches(regex);
    }

    public boolean checkField(Notification notificationEntity) {
        if ((notificationEntity.getDestination() == null) ||
                (notificationEntity.getDestination().equals("")) ||
                (!checkFieldMail(notificationEntity.getDestination()))) {
            return false;
        }
        if ((notificationEntity.getSubject() == null) ||
                (notificationEntity.getSubject().equals(""))) {
            return false;
        }
        if ((notificationEntity.getBody() == null) ||
                (notificationEntity.getBody().equals(""))){
            return false;
        }

        return true;
    }
}
