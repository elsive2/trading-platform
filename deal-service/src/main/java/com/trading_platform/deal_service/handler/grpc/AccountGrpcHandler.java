package com.trading_platform.deal_service.handler.grpc;

import com.trading_platform.deal_service.service.AccountService;
import com.trading_platform.grpc.account.AccountInformationResponse;
import com.trading_platform.grpc.account.AccountRequest;
import com.trading_platform.grpc.account.AccountServiceGrpc;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.trading_platform.deal_service.interceptor.JwtServerInterceptor.JWT_CLAIMS;

@RequiredArgsConstructor
@Component
@Slf4j
public class AccountGrpcHandler extends AccountServiceGrpc.AccountServiceImplBase {
    private final AccountService accountService;

    @Override
    public void createAccount(AccountRequest req, StreamObserver<AccountInformationResponse> responseObserver) {
        log.info("Creating account");

        Claims claims = JWT_CLAIMS.get();

        if (Objects.isNull(claims)) {
            responseObserver.onError(io.grpc.Status.UNAUTHENTICATED.withDescription("No JWT in context").asRuntimeException());
            return;
        }

        String userId = (String) claims.get("id");

        accountService.findOrCreateByUserId(userId)
                .map(this::mapToGrpc)
                .doOnNext(responseObserver::onNext)
                .doOnSuccess(v -> responseObserver.onCompleted())
                .doOnError(e -> {
                    log.error("Error creating account", e);
                    responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
                })
                .subscribe();
    }

    private AccountInformationResponse mapToGrpc(com.trading_platform.deal_service.dto.AccountInformationResponse dto) {
        var responseBuilder = AccountInformationResponse.newBuilder()
                .setId(dto.id())
                .setBalance(dto.balance());

        dto.holdings().forEach(holding -> responseBuilder.addHoldings(
                com.trading_platform.grpc.account.Holding.newBuilder()
                        .setStockId(holding.getStockId())
                        .setQuantity(holding.getQuantity())
                        .build()
        ));

        return responseBuilder.build();
    }
}
