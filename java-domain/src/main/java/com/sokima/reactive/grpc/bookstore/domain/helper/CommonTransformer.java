package com.sokima.reactive.grpc.bookstore.domain.helper;

import com.sokima.reactive.grpc.bookstore.domain.ImmutableIsbn;
import com.sokima.reactive.grpc.bookstore.domain.Isbn;

public final class CommonTransformer {
    public Isbn mapToIsbn(final String value) {
        return ImmutableIsbn.builder().isbn(value).build();
    }
}
