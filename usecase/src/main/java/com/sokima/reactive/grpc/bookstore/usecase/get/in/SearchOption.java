package com.sokima.reactive.grpc.bookstore.usecase.get.in;

public interface SearchOption<S> {
    S option();

    String type();
}
