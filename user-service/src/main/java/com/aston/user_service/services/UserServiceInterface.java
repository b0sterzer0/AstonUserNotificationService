package com.aston.user_service.services;

import com.aston.user_service.dto.UserDto;

public interface UserServiceInterface {
    UserDto getUser(long id);
    UserDto createUser(UserDto userDto);
    UserDto updateUser(long id, UserDto userDto);
    void deleteUser(long id);
}
