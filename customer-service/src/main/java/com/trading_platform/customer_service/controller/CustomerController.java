package com.trading_platform.customer_service.controller;

import com.trading_platform.customer_service.dto.CustomerInformation;
import com.trading_platform.customer_service.service.CustomerService;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Mono<CustomerInformation> findById(@PathVariable Integer id) {
        return service.findById(id);
    }
}
