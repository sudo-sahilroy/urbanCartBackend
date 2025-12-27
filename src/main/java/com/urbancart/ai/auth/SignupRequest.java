package com.urbancart.ai.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "Full name is required") String fullName,
        @Email @NotBlank String email,
        @Size(min = 6, message = "Password must be at least 6 characters") String password
) {}
