package com.trading_platform.authorization_service.service;

import com.trading_platform.authorization_service.interceptor.JwtClientInterceptor;
import com.trading_platform.grpc.account.AccountInformationResponse;
import com.trading_platform.grpc.account.AccountRequest;
import com.trading_platform.grpc.account.AccountServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountServiceGrpc.AccountServiceBlockingStub stub;

    public AccountInformationResponse createAccount(String token) {
                return stub.withInterceptors(new JwtClientInterceptor(token))
                        .createAccount(AccountRequest.newBuilder().build());
    }
}
