package com.aston.notification_service.services;

import com.aston.notification_service.dto.UserEventDto;

import java.util.Map;

public interface NotificationServiceInterface {
    Map<String, String> userCreatedEventEmailNotification(UserEventDto userEventDto);
    Map<String, String> userDeletedEventEmailNotification(UserEventDto userEventDto);
}
