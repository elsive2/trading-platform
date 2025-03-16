package com.trading_platform.authorization_service.mapper;

import com.trading_platform.authorization_service.dto.request.UserResponse;
import com.trading_platform.authorization_service.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponseFromEntity(final User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }
}
