package com.sokima.reactive.grpc.bookstore.domain.oneof;

public interface OneofResolver<I, O> {
    O resolve(final I oneof);
}
