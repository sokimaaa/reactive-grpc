package com.sokima.reactive.grpc.bookstore.domain.helper;

import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;

public record Baggage<T>(
        BookIdentity bookIdentity,
        T value
) {
    public static <T> Baggage<T> of(final BookIdentity bookIdentity, final T value) {
        return new Baggage<>(bookIdentity, value);
    }
}
