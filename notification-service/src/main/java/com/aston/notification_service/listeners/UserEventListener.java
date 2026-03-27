package com.aston.notification_service.listeners;

import com.aston.notification_service.annotations.Loggable;
import com.aston.notification_service.dto.UserEventDto;
import com.aston.notification_service.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {
    NotificationService notificationService;

    @Autowired
    public UserEventListener(NotificationService notificationServices) {
        this.notificationService = notificationServices;
    }

    @Loggable
    @KafkaListener(topics = "user.created")
    public void handleUserCreated(UserEventDto userEventDto) {
        notificationService.userCreatedEventEmailNotification(userEventDto);
    }

    @Loggable
    @KafkaListener(topics = "user.deleted")
    public void handleUserDeleted(UserEventDto userEventDto) {
        notificationService.userDeletedEventEmailNotification(userEventDto);
    }
}
