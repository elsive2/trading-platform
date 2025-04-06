package com.trading_platform.deal_service.dto;

import java.util.List;

public record AccountInformationResponse(
        Integer id,
        Integer userId,
        Integer balance,
        List<Holding> holdings
) {
}
