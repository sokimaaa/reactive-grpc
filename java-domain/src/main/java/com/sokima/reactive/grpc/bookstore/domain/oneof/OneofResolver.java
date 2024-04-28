package com.sokima.reactive.grpc.bookstore.domain.oneof;

import static java.lang.String.format;

public interface OneofResolver<I, O> {
    O resolve(final I oneof);

    OneofResolver<I, O> next();

    static <I, O> OneofResolver<I, O> dummyOneofResolver() {
        return new OneofResolver<>() {
            @Override
            public O resolve(final I oneof) {
                throw new UnsupportedOperationException(format("Any resolvers that could process oneof like: %s", oneof));
            }

            @Override
            public OneofResolver<I, O> next() {
                return this;
            }
        };
    }
}
