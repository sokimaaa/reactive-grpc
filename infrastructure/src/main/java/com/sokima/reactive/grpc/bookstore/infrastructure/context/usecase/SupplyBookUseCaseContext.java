package com.sokima.reactive.grpc.bookstore.infrastructure.context.usecase;

import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.supply.SupplyBookFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupplyBookUseCaseContext {
    @Bean
    SupplyBookFacade supplyBookFacade(final FindBookPort findBookPort, final CreateBookPort createBookPort) {
        return new SupplyBookFacade(findBookPort, createBookPort);
    }
}
