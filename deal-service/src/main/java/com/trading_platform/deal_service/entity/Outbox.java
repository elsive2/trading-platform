package com.trading_platform.deal_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Outbox {
    @Id
    private UUID id;
    private String payload;
    private Instant createdAt;
}
