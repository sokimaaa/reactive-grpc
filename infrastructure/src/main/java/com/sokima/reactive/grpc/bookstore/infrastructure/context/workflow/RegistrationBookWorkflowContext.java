package com.sokima.reactive.grpc.bookstore.infrastructure.context.workflow;

import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.registration.RegistrationBookFlow;
import com.sokima.reactive.grpc.bookstore.usecase.registration.in.RegistrationBookInformation;
import com.sokima.reactive.grpc.bookstore.usecase.registration.mapper.Checksum2RegistrationFlowResultMapper;
import com.sokima.reactive.grpc.bookstore.usecase.registration.out.RegistrationBookFlowResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegistrationBookWorkflowContext {

    @Bean
    Flow<RegistrationBookInformation, RegistrationBookFlowResult> registrationBookFlow(
            final CreateBookPort createBookPort,
            final Checksum2RegistrationFlowResultMapper checksumMapper
    ) {
        return new RegistrationBookFlow(createBookPort, checksumMapper);
    }

    @Bean
    Checksum2RegistrationFlowResultMapper checksum2RegistrationFlowResultMapper() {
        return new Checksum2RegistrationFlowResultMapper();
    }
}
