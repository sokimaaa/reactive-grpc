package com.sokima.reactive.grpc.bookstore.infrastructure.context.workflow;

import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.supply.SupplyBookFlow;
import com.sokima.reactive.grpc.bookstore.usecase.supply.in.SupplyBookInformation;
import com.sokima.reactive.grpc.bookstore.usecase.supply.out.SupplyBookFlowResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupplyBookWorkflowContext {

    @Bean
    Flow<SupplyBookInformation, SupplyBookFlowResult> supplyBookFlow(
            final FindBookPort findBookPort,
            final CreateBookPort createBookPort
    ) {
        return new SupplyBookFlow(findBookPort, createBookPort);
    }
}
