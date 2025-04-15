package com.trading_platform.authorization_service.service;

import com.trading_platform.authorization_service.dto.request.AuthRequest;
import com.trading_platform.authorization_service.dto.request.RegisterRequest;
import com.trading_platform.authorization_service.dto.response.AuthResponse;
import com.trading_platform.authorization_service.entity.User;
import com.trading_platform.authorization_service.exception.UserAlreadyExistsException;
import com.trading_platform.authorization_service.repository.UserRepository;
import com.trading_platform.enums.Role;
import com.trading_platform.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final ReactiveAuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    private final AccountService accountService;

    // @TODO: Cacheable
    public Mono<AuthResponse> auth(Mono<AuthRequest> request) {
        return request.flatMap(authRequest -> authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )))
                .map(authentication -> (UserDetails) authentication.getPrincipal())
                .flatMap(userDetails -> userRepository.findByUsername(userDetails.getUsername()))
                .map(user -> new AuthResponse(generateToken(user)));
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
                .map(user -> new AuthResponse(generateToken(user)))
                .doOnNext(response -> accountService.createAccount(response.getToken()))
        );
    }

    public String generateToken(User user) {
        List<String> authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authorities);
        claims.put("id", user.getId());
        return jwtUtil.doGenerateToken(claims, user.getUsername());
    }
}
