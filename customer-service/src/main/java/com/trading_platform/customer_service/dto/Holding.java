package com.trading_platform.customer_service.dto;

import com.trading_platform.customer_service.enums.TickerEnum;

public record Holding(TickerEnum tickerEnum, int quantity) {
}
