package com.urbancart.ai.auth;

import com.urbancart.ai.users.UserDto;

public record AuthResponse(String accessToken, String refreshToken, UserDto user) {}
