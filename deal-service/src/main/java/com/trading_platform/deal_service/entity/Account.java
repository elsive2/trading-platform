package com.trading_platform.deal_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Data
public class Account {
    @Id
    private Integer id;
    private String userId;
    private Integer balance;
}

