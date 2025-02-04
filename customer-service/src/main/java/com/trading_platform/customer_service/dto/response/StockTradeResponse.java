package com.trading_platform.customer_service.dto.response;

import com.trading_platform.customer_service.enums.TickerEnum;
import com.trading_platform.customer_service.enums.TradeActionEnum;

public record StockTradeResponse(
        Integer customerId,
        TickerEnum ticker,
        Integer price,
        Integer quantity,
        TradeActionEnum tradeAction,
        Integer totalPrice,
        Integer balance
) {
}
