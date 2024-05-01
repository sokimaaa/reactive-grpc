package com.sokima.reactive.grpc.bookstore.usecase.purchase.processor;

import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookFlowResult;
import reactor.core.publisher.Flux;

public class ErrorPurchaseOptionProcessor<P extends PurchaseOption<?>> implements PurchaseOptionProcessor<P> {
    @Override
    public Flux<PurchaseBookFlowResult> process(final P purchaseOption) {
        throw new UnsupportedOperationException("Fallback processor. Unknown type of search book option: " + purchaseOption.option());
    }

    @Override
    public boolean support(final String type) {
        return false;
    }
}
