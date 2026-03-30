package com.aston.notification_service.controllers;

import com.aston.notification_service.dto.UserEventDto;
import com.aston.notification_service.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/notification/email")
public class EmailNotificationController {
    private final NotificationService notificationService;

    public EmailNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/user/created")
    public ResponseEntity<Map<String, String>> userCreationEmailNotification(@RequestBody UserEventDto userEventDto) {
        Map<String, String> result = notificationService.userCreatedEventEmailNotification(userEventDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("user/deleted")
    public ResponseEntity<Map<String, String>> userDeletionEmailNotification(@RequestBody UserEventDto userEventDto) {
        Map<String, String> result = notificationService.userDeletedEventEmailNotification(userEventDto);
        return ResponseEntity.ok(result);
    }
}
