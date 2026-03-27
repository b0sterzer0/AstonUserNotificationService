package com.aston.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserEventDto(
        @NotNull @Positive Long id,
        @NotNull @Email String email
) {
}
