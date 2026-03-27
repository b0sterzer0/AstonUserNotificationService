package com.aston.user_service.mappers;

import com.aston.user_service.dto.UserDto;
import com.aston.user_service.models.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
    }

    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setAge(userDto.age());
        user.setCreatedAt(userDto.createdAt());
        return user;
    }

    public UserDto addCreatedAtToDto(UserDto userDto, LocalDateTime createdAt) {
        return new UserDto(userDto.id(), userDto.name(), userDto.email(), userDto.age(), createdAt);
    }
}
