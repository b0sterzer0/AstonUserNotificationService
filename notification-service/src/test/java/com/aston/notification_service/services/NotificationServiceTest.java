package com.aston.notification_service.services;

import com.aston.notification_service.dto.UserEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationServiceTest {
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService();
    }

    @Test
    void userCreatedEventEmailNotification_shouldReturnCorrectResponse() {
        UserEventDto dto = new UserEventDto(1L, "test@example.com");

        Map<String, String> result =
                notificationService.userCreatedEventEmailNotification(dto);

        assertEquals("success", result.get("status"));
        assertEquals("test@example.com", result.get("email"));
        assertEquals(
                "Здравствуйте! Ваш аккаунт на нашем сайте успешно создан!",
                result.get("message")
        );
    }

    @Test
    void userDeletedEventEmailNotification_shouldReturnCorrectResponse() {
        UserEventDto dto = new UserEventDto(2L, "delete@example.com");

        Map<String, String> result =
                notificationService.userDeletedEventEmailNotification(dto);

        assertEquals("success", result.get("status"));
        assertEquals("delete@example.com", result.get("email"));
        assertEquals(
                "Здравствуйте! Ваш аккаунт был удален!",
                result.get("message")
        );
    }
}
