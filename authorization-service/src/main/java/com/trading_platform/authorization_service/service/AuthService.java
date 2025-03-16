package com.trading_platform.authorization_service.service;

import com.trading_platform.authorization_service.dto.request.AuthRequest;
import com.trading_platform.authorization_service.dto.response.AuthResponse;
import com.trading_platform.authorization_service.filter.JwtFilter;
import com.trading_platform.authorization_service.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class AuthService {
    private final ReactiveAuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    public Mono<AuthResponse> auth(Mono<AuthRequest> request) {
        return request.flatMap(authRequest -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )))
                .map(authentication -> (UserDetails) authentication.getPrincipal())
                .map(userDetails -> new AuthResponse(jwtUtil.generateToken(userDetails)));
    }
}
