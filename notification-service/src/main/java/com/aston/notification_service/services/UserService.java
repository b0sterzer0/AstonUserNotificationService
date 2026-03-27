package com.aston.notification_service.services;

import com.aston.notification_service.dto.UserEventDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public void userCreatedEventFromKafkaHandler(UserEventDto userEventDto) {
        System.out.println("Отправляю сообщение на почту:  " + userEventDto.email());
        System.out.println("Здравствуйте! Ваш аккаунт на нашем сайте успешно создан!");
    }

    public void userDeletedEventFromKafkaHandler(UserEventDto userEventDto) {
        System.out.println("Отправляю сообщение на почту:  " + userEventDto.email());
        System.out.println("Здравствуйте! Ваш аккаунт был удален!");
    }
}
