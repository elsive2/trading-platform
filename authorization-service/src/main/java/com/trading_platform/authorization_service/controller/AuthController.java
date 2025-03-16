package com.trading_platform.authorization_service.controller;

import com.trading_platform.authorization_service.dto.request.AuthRequest;
import com.trading_platform.authorization_service.dto.response.AuthResponse;
import com.trading_platform.authorization_service.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;


    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody Mono<AuthRequest> authRequest) {
        return authService.auth(authRequest);
    }
}
