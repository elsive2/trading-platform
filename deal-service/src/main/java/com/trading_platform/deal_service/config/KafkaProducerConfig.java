package com.trading_platform.deal_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;
import reactor.kafka.sender.SenderOptions;

@RequiredArgsConstructor
@Configuration
public class KafkaProducerConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public SenderOptions<String, String> senderOptions(KafkaProperties kafkaProperties) {
        return SenderOptions.<String, String>create(kafkaProperties.buildProducerProperties(null))
                .withValueSerializer(new JsonSerializer<>(objectMapper));
    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> producerTemplate(SenderOptions<String, String> options) {
        return new ReactiveKafkaProducerTemplate<>(options);
    }
}
