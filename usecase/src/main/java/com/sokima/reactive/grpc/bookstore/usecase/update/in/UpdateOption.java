package com.sokima.reactive.grpc.bookstore.usecase.update.in;

public interface UpdateOption {
    String field();

    String value();

    String checksum();
}
