package com.sokima.reactive.grpc.bookstore.usecase.purchase.processor;

import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookFlowResult;
import reactor.core.publisher.Flux;

public interface PurchaseOptionProcessor<S extends PurchaseOption<?>> {
    Flux<PurchaseBookFlowResult> process(final S purchaseOption);

    boolean support(final String type);
}
