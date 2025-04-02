package com.trading_platform.authentication;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RequestContextFilter implements Filter {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_ROLES = "X-User-Roles";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            if (servletRequest instanceof HttpServletRequest request) {
                String userId = request.getHeader(HEADER_USER_ID);
                String rolesHeader = request.getHeader(HEADER_USER_ROLES);

                Set<String> roles = rolesHeader != null
                        ? new HashSet<>(Arrays.asList(rolesHeader.split(",")))
                        : Set.of();

                RequestContextHolder.set(new RequestContext(userId, roles));
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            RequestContextHolder.clear();
        }
    }
}
