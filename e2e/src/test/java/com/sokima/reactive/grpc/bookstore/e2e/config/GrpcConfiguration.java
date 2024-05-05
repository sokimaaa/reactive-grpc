package com.sokima.reactive.grpc.bookstore.e2e.config;

import com.sokima.reactive.grpc.bookstore.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfiguration {

    @Bean
    ManagedChannel managedChannel() {
        return NettyChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
    }

    @Bean
    ReactorGetBookUseCaseGrpc.ReactorGetBookUseCaseStub getBookUseCaseStub(final ManagedChannel managedChannel) {
        return ReactorGetBookUseCaseGrpc.newReactorStub(managedChannel);
    }

    @Bean
    ReactorPurchaseBookUseCaseGrpc.ReactorPurchaseBookUseCaseStub purchaseBookUseCaseStub(final ManagedChannel managedChannel) {
        return ReactorPurchaseBookUseCaseGrpc.newReactorStub(managedChannel);
    }

    @Bean
    ReactorRegistrationBookUseCaseGrpc.ReactorRegistrationBookUseCaseStub registrationBookUseCaseStub(final ManagedChannel managedChannel) {
        return ReactorRegistrationBookUseCaseGrpc.newReactorStub(managedChannel);
    }

    @Bean
    ReactorSupplyBookUseCaseGrpc.ReactorSupplyBookUseCaseStub supplyBookUseCaseStub(final ManagedChannel managedChannel) {
        return ReactorSupplyBookUseCaseGrpc.newReactorStub(managedChannel);
    }

    @Bean
    ReactorUpdateBookUseCaseGrpc.ReactorUpdateBookUseCaseStub updateBookUseCaseStub(final ManagedChannel managedChannel) {
        return ReactorUpdateBookUseCaseGrpc.newReactorStub(managedChannel);
    }
}
