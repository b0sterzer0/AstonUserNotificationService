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
        // given
        long id = 1L;
        User user = new User();
        UserDto expectedDto = new UserDto(id, "Ivan", "ivan@mail.com", 25, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expectedDto);

        // when
        UserDto result = userService.getUser(id);

        // then
        assertNotNull(result);
        assertEquals("Ivan", result.name());
        verify(userRepository).findById(id);
    }

    @Test
    void getUser_ShouldThrowException_WhenIdNotFound() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    void createUser_ShouldSaveAndSendKafkaMessage() {
        // given
        UserDto inputDto = new UserDto(null, "Ivan", "ivan@mail.com", 25, null);
        User user = new User();
        UserEventDto eventDto = new UserEventDto(1L, "ivan@mail.com");

        when(userMapper.addCreatedAtToDto(eq(inputDto), any())).thenReturn(inputDto);
        when(userMapper.toEntity(inputDto)).thenReturn(user);
        when(userEventMapper.toDto(user)).thenReturn(eventDto);
        when(userMapper.toDto(user)).thenReturn(inputDto);

        // when
        userService.createUser(inputDto);

        // then
        verify(userRepository).save(user); // Проверяем сохранение в БД
        verify(kafkaTemplate).send(eq("user.created"), eq(eventDto)); // Проверяем отправку в Kafka
    }

    @Test
    void deleteUser_ShouldDeleteAndSendKafkaMessage() {
        // given
        long id = 1L;
        User user = new User();
        UserEventDto eventDto = new UserEventDto(id, "deleted@mail.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userEventMapper.toDto(user)).thenReturn(eventDto);

        // when
        userService.deleteUser(id);

        // then
        verify(userRepository).delete(user);
        verify(kafkaTemplate).send(eq("user.deleted"), eq(eventDto));
    }
}
