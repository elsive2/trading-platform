package com.trading_platform.deal_service.interceptor;

import com.trading_platform.util.JwtUtil;
import io.grpc.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.stereotype.Component;

@GlobalServerInterceptor
@RequiredArgsConstructor
@Component
public class JwtServerInterceptor implements ServerInterceptor {
    private final JwtUtil jwtUtil;
    public static final Context.Key<Claims> JWT_CLAIMS = Context.key("jwt");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> serverCall,
            Metadata metadata,
            ServerCallHandler<ReqT, RespT> serverCallHandler
    ) {
        String authHeader = metadata.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            serverCall.close(Status.UNAUTHENTICATED.withDescription("Missing or invalid Authorization header"), new Metadata());
            return new ServerCall.Listener<>() {};
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtUtil.getAllClaimsFromToken(token);

            if (!jwtUtil.validateToken(token)) {
                throw new JwtException("Token expired or invalid");
            }

            Context ctx = Context.current().withValue(JWT_CLAIMS, claims);
            return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);

        } catch (JwtException e) {
            serverCall.close(Status.UNAUTHENTICATED.withDescription("Invalid token").withCause(e), new Metadata());
            return new ServerCall.Listener<>() {};
        }
    }
}
