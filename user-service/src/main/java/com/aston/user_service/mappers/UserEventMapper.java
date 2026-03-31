package com.aston.user_service.mappers;

import com.aston.user_service.dto.UserEventDto;
import com.aston.user_service.dto.UserEventType;
import com.aston.user_service.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserEventMapper {
    public UserEventDto toDto(User user, UserEventType eventType) {
        return new UserEventDto(user.getId(), user.getEmail(), eventType);
    }
}
