package com.trading_platform.authorization_service.config;

import com.trading_platform.grpc.account.AccountServiceGrpc;
import com.trading_platform.grpc.account.DealServiceProto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcConfig {
    @Bean
    AccountServiceGrpc.AccountServiceBlockingStub stub(GrpcChannelFactory channels) {
        return AccountServiceGrpc.newBlockingStub(channels.createChannel("local"));
    }
}
