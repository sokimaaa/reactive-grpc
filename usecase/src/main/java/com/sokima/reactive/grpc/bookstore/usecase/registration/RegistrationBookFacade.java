package com.sokima.reactive.grpc.bookstore.usecase.registration;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.registration.out.ImmutableRegistrationBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.registration.out.RegistrationBookResponse;
import reactor.core.publisher.Mono;

public final class RegistrationBookFacade {

    private final CreateBookPort createBookPort;

    public RegistrationBookFacade(final CreateBookPort createBookPort) {
        this.createBookPort = createBookPort;
    }

    public Mono<RegistrationBookResponse> doRegistration(final RegistrationBookRequest registrationBookRequest) {
        return createBookPort.createBookIdentity(
                registrationBookRequest.getTitle(),
                registrationBookRequest.getAuthor(),
                registrationBookRequest.getEdition()
        ).map(
                bookIdentity -> ChecksumGenerator.generateBookChecksum(
                        bookIdentity.title(),
                        bookIdentity.author(),
                        bookIdentity.edition())
        ).flatMap(
                checksum -> createBookPort.createEmptyBookAggregation(checksum)
                        .map(x -> checksum)
        ).map(
                checksum -> ImmutableRegistrationBookResponse.builder()
                        .checksum(checksum)
                        .build()
        );
    }
}
