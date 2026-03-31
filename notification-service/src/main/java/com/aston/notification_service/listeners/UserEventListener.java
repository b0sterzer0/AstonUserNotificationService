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
    @KafkaListener(topics = "${kafka.topics.user-events}")
    public void handleUserEvent(UserEventDto userEventDto) {
        switch (userEventDto.eventType()) {
            case CREATED -> notificationService.userCreatedEventEmailNotification(userEventDto);
            case DELETED -> notificationService.userDeletedEventEmailNotification(userEventDto);
        }
    }
}
