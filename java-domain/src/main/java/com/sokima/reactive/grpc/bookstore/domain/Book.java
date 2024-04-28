package com.sokima.reactive.grpc.bookstore.domain;

import org.immutables.value.Value;

@Value.Immutable
public interface Book {
    Isbn isbn();

    BookIdentity bookIdentity();
}
