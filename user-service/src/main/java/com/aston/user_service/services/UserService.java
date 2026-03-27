package com.aston.user_service.services;

import com.aston.user_service.annotations.Loggable;
import com.aston.user_service.dto.UserDto;
import com.aston.user_service.dto.UserEventDto;
import com.aston.user_service.exceptions.UserDeletionException;
import com.aston.user_service.exceptions.UserNotFoundException;
import com.aston.user_service.mappers.UserEventMapper;
import com.aston.user_service.mappers.UserMapper;
import com.aston.user_service.models.User;
import com.aston.user_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KafkaTemplate<String, UserEventDto> kafkaTemplate;
    private final UserEventMapper userEventMapper;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       KafkaTemplate<String, UserEventDto> kafkaTemplate,
                       UserEventMapper userEventMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.userEventMapper = userEventMapper;
    }

    @Loggable
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDto).toList();
    }

    @Loggable
    @Transactional(readOnly = true)
    public UserDto getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    @Loggable
    @Transactional
    public UserDto createUser(UserDto userDto) {
        UserDto newUserDto = userMapper.addCreatedAtToDto(userDto, LocalDateTime.now());
        User user = userMapper.toEntity(newUserDto);
        userRepository.save(user);
        kafkaTemplate.send("user.created", userEventMapper.toDto(user));
        return userMapper.toDto(user);
    }

    @Loggable
    @Transactional
    public UserDto updateUser(long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (userDto.name() != null) user.setName(userDto.name());
        if (userDto.email() != null) user.setEmail(userDto.email());
        if (userDto.age() != null) user.setAge(userDto.age());
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Loggable
    @Transactional
    public void deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserDeletionException(id));
        userRepository.delete(user);
        kafkaTemplate.send("user.deleted", userEventMapper.toDto(user));
    }
}
