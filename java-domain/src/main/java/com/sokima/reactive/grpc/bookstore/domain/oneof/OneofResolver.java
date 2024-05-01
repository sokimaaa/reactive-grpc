package com.sokima.reactive.grpc.bookstore.domain.oneof;

@Deprecated
public interface OneofResolver<I, O> {
    O resolve(final I oneof);
}
