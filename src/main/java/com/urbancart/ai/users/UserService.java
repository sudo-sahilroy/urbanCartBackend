package com.urbancart.ai.users;

import com.urbancart.ai.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserDto.from(user);
    }

    public UserDto updateCurrentUser(UpdateUserRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setFullName(request.fullName());
        user.setPhone(request.phone());
        user.setAddress(request.address());
        user.setAvatarUrl(request.avatarUrl());
        return UserDto.from(userRepository.save(user));
    }
}
