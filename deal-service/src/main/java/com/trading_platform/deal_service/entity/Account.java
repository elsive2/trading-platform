package com.trading_platform.deal_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Account {
    @Id
    private Integer id;
    private Integer userId;
    private Integer balance;
}
