package com.urbancart.ai.users;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank(message = "Full name is required") String fullName,
        String phone,
        String address,
        String avatarUrl
) {}
