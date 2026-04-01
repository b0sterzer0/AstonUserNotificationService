package com.aston.user_service.controllers;

import com.aston.user_service.dto.UserDto;
import com.aston.user_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Users operations")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get User on ID")
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @Operation(summary = "Create new User")
    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        return userService.createUser(userDto);
    }

    @Operation(summary = "Update User on ID")
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable long id, @RequestBody @Valid UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @Operation(summary = "Delete User on ID")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
