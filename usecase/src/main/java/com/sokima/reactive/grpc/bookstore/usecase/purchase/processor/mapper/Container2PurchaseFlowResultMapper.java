package com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.mapper;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.ImmutablePurchaseBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookFlowResult;

public class Container2PurchaseFlowResultMapper {
    public PurchaseBookFlowResult mapToPurchaseBookFlowResult(final UpdateBookPort.Container<Book> bookContainer) {
        return ImmutablePurchaseBookFlowResult.builder()
                .purchasedIsbn(bookContainer.newDomainObject().isbn())
                .build();
    }
}
