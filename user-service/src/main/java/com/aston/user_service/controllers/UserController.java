package com.aston.user_service.controllers;

import com.aston.user_service.dto.UserDto;
import com.aston.user_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public CollectionModel<EntityModel<UserDto>> getUsers() {
        List<EntityModel<UserDto>> users = userService.getAllUsers().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class)
                                .getUser(user.id())).withSelfRel()))
                .toList();

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getUsers()).withSelfRel(),
                linkTo(methodOn(UserController.class).createUser(null)).withRel("create-user")
        );
    }

    @Operation(summary = "Get User on ID")
    @GetMapping("/{id}")
    public EntityModel<UserDto> getUser(@PathVariable long id) {
        UserDto user =  userService.getUser(id);
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUser(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUsers()).withRel("all-users"),
                linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update-user"),
                linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete-user")
        );
    }

    @Operation(summary = "Create new User")
    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> createUser(@RequestBody @Valid UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        return ResponseEntity.created(linkTo(methodOn(UserController.class).getUser(user.id())).toUri()).
                body(EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getUser(user.id())).withSelfRel(),
                        linkTo(methodOn(UserController.class).getUsers()).withRel("all-users"),
                        linkTo(methodOn(UserController.class).updateUser(user.id(), null)).withRel("update-user"),
                        linkTo(methodOn(UserController.class).deleteUser(user.id())).withRel("delete-user")
                        )
                );
    }

    @Operation(summary = "Update User on ID")
    @PutMapping("/{id}")
    public EntityModel<UserDto> updateUser(@PathVariable long id, @RequestBody @Valid UserDto userDto) {
        UserDto user = userService.updateUser(id, userDto);
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUser(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUsers()).withRel("all-users"),
                linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete-user")
        );
    }

    @Operation(summary = "Delete User on ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
