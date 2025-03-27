package com.trading_platform.api_gateway.dto.resonse;

import java.util.List;

public record AuthResponseDto(String id, List<String> roles) {
}
