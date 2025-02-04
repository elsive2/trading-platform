package com.trading_platform.customer_service.dto;

import java.util.List;

public record CustomerInformation(
        Integer id,
        String name,
        Integer balance,
        List<Holding> holdings
) {
}
