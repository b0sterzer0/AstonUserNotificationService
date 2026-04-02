package com.aston.notification_service.controllers;

import com.aston.notification_service.dto.UserEventDto;
import com.aston.notification_service.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/notification/email")
@Tag(name = "Email notifications", description = "Email notification operations")
public class EmailNotificationController {
    private final NotificationService notificationService;

    public EmailNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Send email notification about User creation")
    @PostMapping("/user/created")
    public ResponseEntity<EntityModel<Map<String, String>>> userCreationEmailNotification(@RequestBody UserEventDto userEventDto) {
        Map<String, String> result = notificationService.userCreatedEventEmailNotification(userEventDto);
        return ResponseEntity.ok(EntityModel.of(result,
                linkTo(methodOn(EmailNotificationController.class)
                        .userCreationEmailNotification(userEventDto)).withSelfRel(),
                linkTo(methodOn(EmailNotificationController.class)
                        .userDeletionEmailNotification(userEventDto)).withRel("delete-notification")
                )
        );
    }

    @Operation(summary = "Send email notification about User deletion")
    @PostMapping("user/deleted")
    public ResponseEntity<EntityModel<Map<String, String>>> userDeletionEmailNotification(@RequestBody UserEventDto userEventDto) {
        Map<String, String> result = notificationService.userDeletedEventEmailNotification(userEventDto);
        return ResponseEntity.ok(
                EntityModel.of(result,
                        linkTo(methodOn(EmailNotificationController.class)
                                .userDeletionEmailNotification(userEventDto)).withSelfRel(),
                        linkTo(methodOn(EmailNotificationController.class)
                                .userCreationEmailNotification(userEventDto)).withRel("create-notification")
                )
        );
    }
}
