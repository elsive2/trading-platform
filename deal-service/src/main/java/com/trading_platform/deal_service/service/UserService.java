package com.trading_platform.deal_service.service;

import com.trading_platform.deal_service.dto.AccountInformationResponse;
import com.trading_platform.deal_service.exception.UserNotFoundException;
import com.trading_platform.deal_service.repository.UserRepository;
import com.trading_platform.deal_service.repository.PortfolioItemRepository;
import com.trading_platform.deal_service.dto.Holding;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public UserService(UserRepository userRepository, PortfolioItemRepository portfolioItemRepository) {
        this.userRepository = userRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public Mono<AccountInformationResponse> findById(Integer id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(id)))
                .flatMap(user -> portfolioItemRepository.findByUserId(user.getId())
                            .map(portfolioItem -> new Holding(portfolioItem.getUserId(), portfolioItem.getQuantity()))
                            .collectList()
                            .map(holdings -> new AccountInformationResponse(user.getId(), user.getName(), user.getBalance(), holdings))
                );
    }
}
