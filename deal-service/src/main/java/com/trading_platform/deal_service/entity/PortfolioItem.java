package com.trading_platform.deal_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioItem {
    @Id
    private Integer id;
    private Integer userId;
    private Integer stockId;
    private Integer quantity;
}
