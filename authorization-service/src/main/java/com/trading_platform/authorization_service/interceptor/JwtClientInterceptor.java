package com.trading_platform.authorization_service.interceptor;

import io.grpc.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtClientInterceptor implements ClientInterceptor {
    private final String token;

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> methodDescriptor,
            CallOptions callOptions,
            Channel channel
    ) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                Metadata.Key<String> authKey = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
                headers.put(authKey, "Bearer " + token);
                super.start(responseListener, headers);
            }
        };
    }
}
