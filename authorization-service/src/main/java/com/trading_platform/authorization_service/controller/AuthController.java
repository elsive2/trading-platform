package com.trading_platform.authorization_service.controller;

import com.trading_platform.authorization_service.dto.request.AuthRequest;
import com.trading_platform.authorization_service.dto.request.RegisterRequest;
import com.trading_platform.authorization_service.dto.response.AuthResponse;
import com.trading_platform.authorization_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
