package com.sokima.reactive.grpc.bookstore.usecase.purchase.processor;

import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookResponse;
import reactor.core.publisher.Flux;

public class ErrorPurchaseOptionProcessor implements PurchaseOptionProcessor<PurchaseOption<Object>> {
    @Override
    public Flux<PurchaseBookResponse> process(final PurchaseOption<Object> purchaseOption) {
        throw new UnsupportedOperationException("Fallback processor. Unknown type of search book option: " + purchaseOption.option());
    }

    @Override
    public boolean support(final String type) {
        return false;
    }
}
