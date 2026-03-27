package com.aston.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        @NotBlank String name,
        @Email @NotBlank String email,
        @Min(0) @NotNull Integer age,
        LocalDateTime createdAt
) {
}
