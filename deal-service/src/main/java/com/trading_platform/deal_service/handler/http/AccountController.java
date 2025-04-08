package com.trading_platform.deal_service.handler.http;

import com.trading_platform.authentication.Authorized;
import com.trading_platform.authentication.RequestContext;
import com.trading_platform.authentication.RequestContextHolder;
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

    @GetMapping("/me")
    public Mono<AccountInformationResponse> getAccount() {
        RequestContext requestContext = RequestContextHolder.get();

        return service.findOrCreateByUserId(requestContext.getUserId());
    }
}
