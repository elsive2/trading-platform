package com.trading_platform.stock_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@AllArgsConstructor
public class Stock {
    @Id
    private Integer id;
    private String name;
    private Integer price;
}
