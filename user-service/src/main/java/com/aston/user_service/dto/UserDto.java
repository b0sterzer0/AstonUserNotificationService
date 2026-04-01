package com.aston.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Base User DTO")
public record UserDto(
        @Schema(description = "User ID", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
        Long id,

        @Schema(description = "Username", example = "Test")
        @NotBlank String name,

        @Schema(description = "User email", example = "test@test.com")
        @Email @NotBlank String email,

        @Schema(description = "User age", example = "18")
        @Min(0) @NotNull Integer age,

        @Schema(description = "Created At", accessMode = Schema.AccessMode.READ_ONLY, example = "2026-04-01 00:00:00")
        LocalDateTime createdAt
) {
}
