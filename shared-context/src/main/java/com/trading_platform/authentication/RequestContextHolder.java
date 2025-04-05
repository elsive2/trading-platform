package com.trading_platform.authentication;

public class RequestContextHolder {
    private static final ThreadLocal<RequestContext> context = new ThreadLocal<>();

    public static void set(RequestContext requestContext) {
        context.set(requestContext);
    }

    public static RequestContext get() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }

}
