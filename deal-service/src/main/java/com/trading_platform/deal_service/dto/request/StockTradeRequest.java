package com.trading_platform.deal_service.dto.request;

import com.trading_platform.deal_service.enums.TickerEnum;
import com.trading_platform.deal_service.enums.TradeActionEnum;
import lombok.Data;

@Data
public class StockTradeRequest {
    private Integer userId;
    private TickerEnum ticker;
    private Integer stockId;
    private Integer quantity;
    private TradeActionEnum tradeAction;
}
