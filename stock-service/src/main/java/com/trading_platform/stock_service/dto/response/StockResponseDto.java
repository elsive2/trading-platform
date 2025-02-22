package com.trading_platform.stock_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockResponseDto {
    private Integer id;
    private String ticker;
    private Integer price;
}
