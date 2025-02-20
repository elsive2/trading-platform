package com.trading_platform.stock_service.dto;

import com.trading_platform.stock_service.enums.TickerEnum;

public record PriceResultDto(TickerEnum ticker, Integer price) {
}
