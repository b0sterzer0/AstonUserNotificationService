package com.aston.user_service.services;

import com.aston.user_service.dto.UserDto;
import com.aston.user_service.dto.UserEventDto;
import com.aston.user_service.exceptions.UserNotFoundException;
import com.aston.user_service.mappers.UserEventMapper;
import com.aston.user_service.mappers.UserMapper;
import com.aston.user_service.models.User;
import com.aston.user_service.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final String USER_NAME = "Ivan";
    private static final String EMAIL = "ivan@mail.com";
    private static final int AGE = 25;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private KafkaTemplate<String, UserEventDto> kafkaTemplate;
    @Mock
    private UserEventMapper userEventMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getUser_ShouldReturnUser_WhenIdExists() {
        long id = 1L;
        User user = new User();
        UserDto expectedDto = new UserDto(id, USER_NAME, EMAIL, AGE, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expectedDto);

        UserDto result = userService.getUser(id);

        assertNotNull(result);
        assertEquals(USER_NAME, result.name());
        verify(userRepository).findById(id);
    }

    @Test
    void getUser_ShouldThrowException_WhenIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    void createUser_ShouldSaveAndSendKafkaMessage() {
        UserDto inputDto = new UserDto(null, USER_NAME, EMAIL, AGE, null);
        User user = new User();
        UserEventDto eventDto = new UserEventDto(1L, EMAIL, "CREATED");

        when(userMapper.addCreatedAtToDto(eq(inputDto), any())).thenReturn(inputDto);
        when(userMapper.toEntity(inputDto)).thenReturn(user);
        when(userEventMapper.toDto(user, "CREATED")).thenReturn(eventDto);
        when(userMapper.toDto(user)).thenReturn(inputDto);

        userService.createUser(inputDto);

        verify(userRepository).save(user);
        verify(kafkaTemplate).send(eq("user.events"), eq(eventDto));
    }

    @Test
    void deleteUser_ShouldDeleteAndSendKafkaMessage() {
        long id = 1L;
        User user = new User();
        UserEventDto eventDto = new UserEventDto(id, "deleted@mail.com", "DELETED");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userEventMapper.toDto(user, "DELETED")).thenReturn(eventDto);

        userService.deleteUser(id);

        verify(userRepository).delete(user);
        verify(kafkaTemplate).send(eq("user.events"), eq(eventDto));
    }
}
