package com.trading_platform.authorization_service.controller;

import com.trading_platform.authorization_service.dto.request.AuthRequest;
import com.trading_platform.authorization_service.dto.response.AuthResponse;
import com.trading_platform.authorization_service.service.PBKDF2Encoder;
import com.trading_platform.authorization_service.service.UserService;
import com.trading_platform.authorization_service.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class AuthController {
    private JwtUtil jwtUtil;
    private PBKDF2Encoder encoder;
    private UserService userService;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        return userService.findByUsername(authRequest.getUsername())
                .filter(user -> encoder.matches(authRequest.getPassword(), user.getPassword()))
                .map(user -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(user))))
                .defaultIfEmpty(ResponseEntity.status(401).build());
    }
}
