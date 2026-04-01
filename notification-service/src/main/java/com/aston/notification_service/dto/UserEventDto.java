package com.aston.notification_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "User events DTO")
public record UserEventDto(
        @Schema(description = "User ID", example = "1")
        @NotNull @Positive Long id,

        @Schema(description = "User email", example = "test@test.com")
        @NotNull @Email String email,

        @Schema(description = "Event type", implementation = UserEventType.class)
        @NotNull UserEventType eventType
) {}
