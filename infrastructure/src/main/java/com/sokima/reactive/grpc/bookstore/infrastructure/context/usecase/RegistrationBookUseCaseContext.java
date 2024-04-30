package com.sokima.reactive.grpc.bookstore.infrastructure.context.usecase;

import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.registration.RegistrationBookFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegistrationBookUseCaseContext {
    @Bean
    RegistrationBookFacade registrationBookFacade(final CreateBookPort createBookPort) {
        return new RegistrationBookFacade(createBookPort);
    }
}
