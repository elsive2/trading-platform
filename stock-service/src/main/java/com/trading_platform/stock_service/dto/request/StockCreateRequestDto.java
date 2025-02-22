package com.trading_platform.stock_service.dto.request;

import lombok.Data;

@Data
public class StockCreateRequestDto {
    private String name;
    private Integer price;
}
