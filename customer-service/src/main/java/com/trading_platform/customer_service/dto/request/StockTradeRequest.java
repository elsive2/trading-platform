package com.trading_platform.customer_service.dto.request;

import com.trading_platform.customer_service.enums.TickerEnum;
import com.trading_platform.customer_service.enums.TradeActionEnum;
import lombok.Data;

@Data
public class StockTradeRequest {
    private Integer customerId;
    private TickerEnum ticker;
    private Integer stockId;
    private Integer quantity;
    private TradeActionEnum tradeAction;
}
