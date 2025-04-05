package com.trading_platform.deal_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class User {
    @Id
    private Integer id;
    private String name;
    private Integer balance;
}
