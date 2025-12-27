package com.urbancart.ai.users;

public record UserDto(Long id, String fullName, String email, String phone, String address, String avatarUrl) {
    public static UserDto from(UserEntity user) {
        return new UserDto(user.getId(), user.getFullName(), user.getEmail(), user.getPhone(), user.getAddress(), user.getAvatarUrl());
    }
}
