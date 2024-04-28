package com.sokima.reactive.grpc.bookstore.usecase.purchase.in;

public interface PurchaseOption<O> {
    O option();

    String type();
}
