package com.sokima.reactive.grpc.bookstore.usecase.registration.in;

import com.sokima.reactive.grpc.bookstore.domain.PartialBookIdentity;
import org.immutables.value.Value;

@Value.Immutable
public interface RegistrationBookInformation {
    PartialBookIdentity partialBookIdentity();
}
