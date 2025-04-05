package com.trading_platform.authorization_service.controller;

import com.trading_platform.authorization_service.dto.request.AuthRequest;
import com.trading_platform.authorization_service.dto.request.RegisterRequest;
import com.trading_platform.authorization_service.dto.request.UserResponse;
import com.trading_platform.authorization_service.dto.response.AuthResponse;
import com.trading_platform.authorization_service.entity.User;
import com.trading_platform.authorization_service.mapper.UserMapper;
import com.trading_platform.authorization_service.service.AuthService;
import com.trading_platform.authorization_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/auth")
@AllArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public Mono<AuthResponse> register(@RequestBody @Valid Mono<RegisterRequest> request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody @Valid Mono<AuthRequest> request) {
        return authService.auth(request);
    }

    private final UserMapper userMapper;

    private final UserService userService;

    @GetMapping("/me")
    public Mono<UserResponse> me() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(String.class)
                .flatMap(userService::findByUsername)
                .cast(User.class)
                .map(userMapper::toResponseFromEntity);
    }
}
