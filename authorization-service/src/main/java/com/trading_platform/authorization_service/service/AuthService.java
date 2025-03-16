package com.trading_platform.authorization_service.service;

import com.trading_platform.authorization_service.dto.request.AuthRequest;
import com.trading_platform.authorization_service.dto.request.RegisterRequest;
import com.trading_platform.authorization_service.dto.response.AuthResponse;
import com.trading_platform.authorization_service.entity.User;
import com.trading_platform.authorization_service.enums.Role;
import com.trading_platform.authorization_service.exception.UserAlreadyExistsException;
import com.trading_platform.authorization_service.repository.UserRepository;
import com.trading_platform.authorization_service.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class AuthService {
    private final ReactiveAuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    public Mono<AuthResponse> auth(Mono<AuthRequest> request) {
        return request.flatMap(authRequest -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )))
                .map(authentication -> (UserDetails) authentication.getPrincipal())
                .map(userDetails -> new AuthResponse(jwtUtil.generateToken(userDetails)));
    }

    public Mono<AuthResponse> register(Mono<RegisterRequest> request) {
        return request.flatMap(registerRequest -> userRepository.findByUsername(registerRequest.getUsername())
                .hasElement()
                .flatMap(isExist -> {
                    if (isExist) {
                        return Mono.error(UserAlreadyExistsException::new);
                    }
                    User user = User.builder()
                            .username(registerRequest.getUsername())
                            .password(passwordEncoder.encode(registerRequest.getPassword()))
                            .email(registerRequest.getEmail())
                            .enabled(true)
                            .build();

                    user.addRole(Role.ROLE_USER);

                    return userRepository.save(user);
                })
                .map(user -> new AuthResponse(jwtUtil.generateToken(user)))
        );
    }
}
