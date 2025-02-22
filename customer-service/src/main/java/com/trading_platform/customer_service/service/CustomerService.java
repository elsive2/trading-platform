package com.trading_platform.customer_service.service;

import com.trading_platform.customer_service.dto.CustomerInformation;
import com.trading_platform.customer_service.exception.CustomerNotFoundException;
import com.trading_platform.customer_service.repository.CustomerRepository;
import com.trading_platform.customer_service.repository.PortfolioItemRepository;
import com.trading_platform.customer_service.dto.Holding;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public CustomerService(CustomerRepository customerRepository, PortfolioItemRepository portfolioItemRepository) {
        this.customerRepository = customerRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public Mono<CustomerInformation> findById(Integer id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(id)))
                .flatMap(customer -> portfolioItemRepository.findByCustomerId(customer.getId())
                            .map(portfolioItem -> new Holding(portfolioItem.getCustomerId(), portfolioItem.getQuantity()))
                            .collectList()
                            .map(holdings -> new CustomerInformation(customer.getId(), customer.getName(), customer.getBalance(), holdings))
                );
    }
}
