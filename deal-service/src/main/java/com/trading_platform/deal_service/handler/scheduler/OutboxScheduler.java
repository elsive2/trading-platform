package com.trading_platform.deal_service.handler.scheduler;

import com.trading_platform.deal_service.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final ReactiveKafkaProducerTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    public void publishOutboxMessages() {
        outboxRepository.findAllByOrderByCreatedAtDesc()
                .flatMap(outbox ->
                        kafkaTemplate.send("stock-trade-events", outbox.getPayload())
                                .doOnNext(result -> log.info("Sent to Kafka. Offset: {}", result.recordMetadata().offset()))
                                .then(outboxRepository.deleteById(outbox.getId()))
                                .doOnSuccess(unused -> log.info("Deleted outbox entry: {}", outbox.getId()))
                                .doOnError(e -> log.error("Error sending/deleting outbox {}", outbox.getId(), e))
                )
                .subscribe();
    }
}