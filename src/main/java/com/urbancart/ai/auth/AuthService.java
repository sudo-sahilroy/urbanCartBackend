package com.urbancart.ai.auth;

import com.urbancart.ai.users.UserDto;
import com.urbancart.ai.users.UserEntity;
import com.urbancart.ai.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(BAD_REQUEST, "Email already registered");
        }

        UserEntity user = UserEntity.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .createdAt(Instant.now())
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail());
        String refresh = jwtService.generateRefreshToken(user.getEmail());
        return new AuthResponse(token, refresh, UserDto.from(user));
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserEntity user = userRepository.findByEmail(request.email()).orElseThrow();
        String token = jwtService.generateToken(user.getEmail());
        String refresh = jwtService.generateRefreshToken(user.getEmail());
        return new AuthResponse(token, refresh, UserDto.from(user));
    }

    public AuthResponse refresh(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        UserEntity user = userRepository.findByEmail(username).orElseThrow();
        if (!jwtService.isTokenValid(refreshToken, user.getEmail())) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String token = jwtService.generateToken(user.getEmail());
        String newRefresh = jwtService.generateRefreshToken(user.getEmail());
        return new AuthResponse(token, newRefresh, UserDto.from(user));
    }
}
