package com.sokima.reactive.grpc.bookstore.usecase.get.in;

public interface SearchBookOption<S> {
    S option();

    String type();
}
