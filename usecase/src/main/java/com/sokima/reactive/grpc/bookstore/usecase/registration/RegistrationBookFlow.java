package com.sokima.reactive.grpc.bookstore.usecase.registration;

import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.registration.in.RegistrationBookInformation;
import com.sokima.reactive.grpc.bookstore.usecase.registration.mapper.Checksum2RegistrationFlowResultMapper;
import com.sokima.reactive.grpc.bookstore.usecase.registration.out.RegistrationBookFlowResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public final class RegistrationBookFlow implements Flow<RegistrationBookInformation, RegistrationBookFlowResult> {
    private final CreateBookPort createBookPort;
    private final Checksum2RegistrationFlowResultMapper checksumMapper;

    public RegistrationBookFlow(final CreateBookPort createBookPort, final Checksum2RegistrationFlowResultMapper checksumMapper) {
        this.createBookPort = createBookPort;
        this.checksumMapper = checksumMapper;
    }

    @Override
    public Flux<RegistrationBookFlowResult> doFlow(final RegistrationBookInformation registrationBookInformation) {
        return Mono.just(registrationBookInformation)
                .map(RegistrationBookInformation::partialBookIdentity)
                .flatMap(createBookPort::createBookIdentity)
                .map(ChecksumGenerator::generateBookChecksum)
                .flatMap(createBookPort::createEmptyBookAggregation)
                .map(BookAggregation::checksum)
                .map(checksumMapper::mapToRegistrationFlowResult)
                .flux();
    }
}
