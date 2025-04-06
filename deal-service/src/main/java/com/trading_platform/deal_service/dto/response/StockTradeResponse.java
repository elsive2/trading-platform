package com.trading_platform.deal_service.dto.response;

import com.trading_platform.deal_service.enums.TradeActionEnum;

public record StockTradeResponse(
        Integer accountId,
        Integer price,
        Integer quantity,
        TradeActionEnum tradeAction,
        Integer totalPrice,
        Integer balance
) {
}
