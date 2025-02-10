package com.trading_platform.customer_service.dto;

import com.trading_platform.customer_service.enums.TickerEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Holding {
    private TickerEnum ticker;
    private Integer quantity;
}
