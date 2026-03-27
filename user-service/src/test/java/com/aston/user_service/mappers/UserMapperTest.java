package com.aston.user_service.mappers;

import com.aston.user_service.dto.UserDto;
import com.aston.user_service.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void testToDto() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@mail.com");
        user.setAge(25);
        user.setCreatedAt(now);

        UserDto dto = userMapper.toDto(user);

        assertEquals(user.getId(), dto.id());
        assertEquals(user.getName(), dto.name());
        assertEquals(user.getEmail(), dto.email());
        assertEquals(user.getAge(), dto.age());
        assertEquals(user.getCreatedAt(), dto.createdAt());
    }

    @Test
    void testToEntity() {
        LocalDateTime now = LocalDateTime.now();
        UserDto dto = new UserDto(1L, "John", "john@mail.com", 25, now);

        User user = userMapper.toEntity(dto);

        assertEquals(dto.name(), user.getName());
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.age(), user.getAge());
        assertEquals(dto.createdAt(), user.getCreatedAt());
    }

    @Test
    void testAddCreatedAtToDto() {
        UserDto dto = new UserDto(null, "John", "john@mail.com", 25, null);
        LocalDateTime now = LocalDateTime.now();

        UserDto newDto = userMapper.addCreatedAtToDto(dto, now);

        assertEquals(dto.id(), newDto.id());
        assertEquals(dto.name(), newDto.name());
        assertEquals(dto.email(), newDto.email());
        assertEquals(dto.age(), newDto.age());
        assertEquals(now, newDto.createdAt());
    }
}
