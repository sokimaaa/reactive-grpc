package com.sokima.reactive.grpc.bookstore.usecase.registration.out;

import org.immutables.value.Value;

@Value.Immutable
public interface RegistrationBookResponse {
    String checksum();
}
