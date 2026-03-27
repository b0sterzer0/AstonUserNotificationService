//package com.aston.user_service.services;
//
//import com.aston.user_service.dto.UserDto;
//import com.aston.user_service.exceptions.UserDeletionException;
//import com.aston.user_service.exceptions.UserNotFoundException;
//import com.aston.user_service.mappers.UserMapper;
//import com.aston.user_service.models.User;
//import com.aston.user_service.repositories.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class UserServiceTest {
//    private UserRepository userRepository;
//    private UserMapper userMapper;
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        userRepository = mock(UserRepository.class);
//        userMapper = mock(UserMapper.class);
//        userService = new UserService(userRepository, userMapper);
//    }
//
//    @Test
//    void testGetAllUsers() {
//        User user = new User();
//        user.setId(1L);
//        when(userRepository.findAll()).thenReturn(List.of(user));
//
//        UserDto dto = new UserDto(1L, "John", "john@mail.com", 25, LocalDateTime.now());
//        when(userMapper.toDto(user)).thenReturn(dto);
//
//        List<UserDto> result = userService.getAllUsers();
//
//        assertEquals(1, result.size());
//        assertEquals(dto, result.get(0));
//        verify(userRepository).findAll();
//        verify(userMapper).toDto(user);
//    }
//
//    @Test
//    void testGetUserFound() {
//        User user = new User();
//        user.setId(1L);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        UserDto dto = new UserDto(1L, "John", "john@mail.com", 25, LocalDateTime.now());
//        when(userMapper.toDto(user)).thenReturn(dto);
//
//        UserDto result = userService.getUser(1L);
//
//        assertEquals(dto, result);
//        verify(userRepository).findById(1L);
//        verify(userMapper).toDto(user);
//    }
//
//    @Test
//    void testGetUserNotFound() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
//    }
//
//    @Test
//    void testCreateUser() {
//        UserDto inputDto = new UserDto(null, "John", "john@mail.com", 25, null);
//        LocalDateTime now = LocalDateTime.now();
//        UserDto dtoWithCreatedAt = new UserDto(null, "John", "john@mail.com", 25, now);
//
//        User userEntity = new User();
//        userEntity.setName("John");
//        userEntity.setEmail("john@mail.com");
//        userEntity.setAge(25);
//        userEntity.setCreatedAt(now);
//
//        UserDto savedDto = new UserDto(1L, "John", "john@mail.com", 25, now);
//
//        when(userMapper.addCreatedAtToDto(eq(inputDto), any())).thenReturn(dtoWithCreatedAt);
//        when(userMapper.toEntity(dtoWithCreatedAt)).thenReturn(userEntity);
//        when(userMapper.toDto(userEntity)).thenReturn(savedDto);
//
//        UserDto result = userService.createUser(inputDto);
//
//        assertEquals(savedDto, result);
//        verify(userRepository).save(userEntity);
//    }
//
//    @Test
//    void testUpdateUser() {
//        UserDto updateDto = new UserDto(null, "Jane", "jane@mail.com", 30, null);
//
//        User user = new User();
//        user.setId(1L);
//        user.setName("John");
//        user.setEmail("john@mail.com");
//        user.setAge(25);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        UserDto resultDto = new UserDto(1L, "Jane", "jane@mail.com", 30, LocalDateTime.now());
//        when(userMapper.toDto(user)).thenReturn(resultDto);
//
//        UserDto result = userService.updateUser(1L, updateDto);
//
//        assertEquals(resultDto, result);
//        assertEquals("Jane", user.getName());
//        assertEquals("jane@mail.com", user.getEmail());
//        assertEquals(30, user.getAge());
//        verify(userRepository).save(user);
//    }
//
//    @Test
//    void testUpdateUserNotFound() {
//        UserDto updateDto = new UserDto(null, "Jane", "jane@mail.com", 30, null);
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, updateDto));
//    }
//
//    @Test
//    void testDeleteUserSuccess() {
//        User user = new User();
//        user.setId(1L);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        userService.deleteUser(1L);
//
//        verify(userRepository).delete(user);
//    }
//
//    @Test
//    void testDeleteUserNotFound() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(UserDeletionException.class, () -> userService.deleteUser(1L));
//    }
//}
