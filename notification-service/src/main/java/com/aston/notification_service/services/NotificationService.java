package com.aston.notification_service.services;

import com.aston.notification_service.dto.UserEventDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationService implements NotificationServiceInterface {
    private static final String EMAIL_SENT_EVENT_MESSAGE = "Отправляю сообщение на почту:  ";

    public Map<String, String> userCreatedEventEmailNotification(UserEventDto userEventDto) {
        System.out.println(EMAIL_SENT_EVENT_MESSAGE + userEventDto.email());
        return Map.of(
                "status", "success",
                "email", userEventDto.email(),
                "message", "Здравствуйте! Ваш аккаунт на нашем сайте успешно создан!"
        );
    }

    public Map<String, String> userDeletedEventEmailNotification(UserEventDto userEventDto) {
        System.out.println(EMAIL_SENT_EVENT_MESSAGE + userEventDto.email());
        System.out.println("Здравствуйте! Ваш аккаунт был удален!");
        return Map.of(
                "status", "success",
                "email", userEventDto.email(),
                "message", "Здравствуйте! Ваш аккаунт был удален!"
        );
    }
}
