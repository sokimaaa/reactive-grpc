package com.sokima.reactive.grpc.bookstore.usecase.registration;

import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.registration.in.RegistrationBookInformation;
import com.sokima.reactive.grpc.bookstore.usecase.registration.out.ImmutableRegistrationBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.registration.out.RegistrationBookFlowResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public final class RegistrationBookFlow implements Flow<RegistrationBookInformation, RegistrationBookFlowResult> {
    private final CreateBookPort createBookPort;

    public RegistrationBookFlow(final CreateBookPort createBookPort) {
        this.createBookPort = createBookPort;
    }

    @Override
    public Flux<RegistrationBookFlowResult> doFlow(final RegistrationBookInformation registrationBookInformation) {
        return Mono.just(registrationBookInformation)
                .map(RegistrationBookInformation::partialBookIdentity)
                .flatMap(createBookPort::createBookIdentity)
                .map(ChecksumGenerator::generateBookChecksum)
                .flatMap(createBookPort::createEmptyBookAggregation)
                .map(BookAggregation::checksum)
                .<RegistrationBookFlowResult>map(
                        checksum -> ImmutableRegistrationBookFlowResult.builder()
                                .checksum(checksum)
                                .build()
                )
                .flux();
    }
}
