package com.trading_platform.deal_service.dto;

import com.trading_platform.deal_service.enums.TradeActionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockTradeEvent {
    private int stockId;
    private int totalCost;
    private TradeActionEnum tradeAction;
}
