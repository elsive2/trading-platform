package com.trading_platform.stock_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StockCreateRequestDto {
    private String name;
    private Integer price;
}
