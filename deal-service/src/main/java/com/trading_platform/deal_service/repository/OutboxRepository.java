package com.trading_platform.deal_service.repository;

import com.trading_platform.deal_service.entity.Outbox;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface OutboxRepository extends R2dbcRepository<Outbox, UUID> {
    Flux<Outbox> findAllByOrderByCreatedAtDesc();
}
