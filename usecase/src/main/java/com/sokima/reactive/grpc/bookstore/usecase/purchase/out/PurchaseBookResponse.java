package com.sokima.reactive.grpc.bookstore.usecase.purchase.out;

import com.sokima.reactive.grpc.bookstore.domain.Isbn;
import org.immutables.value.Value;

@Value.Immutable
public interface PurchaseBookResponse {
    Isbn purchasedIsbn();
}
