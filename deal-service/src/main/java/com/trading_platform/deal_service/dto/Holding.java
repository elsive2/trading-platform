package com.trading_platform.deal_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Holding {
    private Integer stockId;
    private Integer quantity;
}
