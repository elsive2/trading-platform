package com.trading_platform.stock_service.mapper;

import com.trading_platform.stock_service.dto.request.StockCreateRequestDto;
import com.trading_platform.stock_service.dto.response.StockResponseDto;
import com.trading_platform.stock_service.entity.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {
    public StockResponseDto toResponse(Stock stock) {
        return new StockResponseDto(stock.getId(), stock.getName(), stock.getPrice());
    }

    public Stock toEntityFromCreateRequest(StockCreateRequestDto stock) {
        return new Stock(null, stock.getName(), stock.getPrice());
    }
}
