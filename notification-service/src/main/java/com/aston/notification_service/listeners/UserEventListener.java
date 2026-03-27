package com.aston.notification_service.listeners;

import com.aston.notification_service.annotations.Loggable;
import com.aston.notification_service.dto.UserEventDto;
import com.aston.notification_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {
    UserService userService;

    @Autowired
    public UserEventListener(UserService userServices) {
        this.userService = userServices;
    }

    @Loggable
    @KafkaListener(topics = "user.created")
    public void handleUserCreated(UserEventDto userEventDto) {
        userService.userCreatedEventFromKafkaHandler(userEventDto);
    }

    @Loggable
    @KafkaListener(topics = "user.deleted")
    public void handleUserDeleted(UserEventDto userEventDto) {
        userService.userDeletedEventFromKafkaHandler(userEventDto);
    }
}
