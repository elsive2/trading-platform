package com.trading_platform.customer_service.entity;

import com.trading_platform.customer_service.enums.TickerEnum;
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
    private Integer customerId;
    private Integer stockId;
    private Integer quantity;
}
