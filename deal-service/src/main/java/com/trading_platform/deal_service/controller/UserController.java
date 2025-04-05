package com.trading_platform.deal_service.controller;

import com.trading_platform.deal_service.dto.AccountInformationResponse;
import com.trading_platform.deal_service.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Mono<AccountInformationResponse> findById(@PathVariable Integer id) {
        return service.findById(id);
    }
}
