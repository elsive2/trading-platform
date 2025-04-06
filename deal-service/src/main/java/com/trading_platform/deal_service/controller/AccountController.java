package com.trading_platform.deal_service.controller;

import com.trading_platform.authentication.Authorized;
import com.trading_platform.deal_service.dto.AccountInformationResponse;
import com.trading_platform.deal_service.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Authorized
@AllArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService service;

    @GetMapping("/{id}")
    public Mono<AccountInformationResponse> findById(@PathVariable Integer id) {
        return service.findById(id);
    }
}
