package com.trading_platform.api_gateway.dto.resonse;

import java.util.List;

public record AuthResponseDto(Integer id, List<String> roles) {
}
